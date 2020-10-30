package com.example.picstagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.picstagram.MainActivity;
import com.example.picstagram.Post;
import com.example.picstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.picstagram.MainActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends Fragment {
  EditText etDescription;
  Button btnUpload;
  Button btnSubmit;
  ImageView ivPosterImage;
  private File photoFile;
  private String photoFileName = "photo.jpg";
  ProgressBar progressBar;
  public static final String TAG = "ComposeFragment";

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
//
//  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public ComposeFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment ComposeFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static ComposeFragment newInstance(String param1, String param2) {
    ComposeFragment fragment = new ComposeFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_compose, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    etDescription = view.findViewById(R.id.etDescription);
    btnUpload = view.findViewById(R.id.btnUpload);
    btnSubmit = view.findViewById(R.id.btnSubmit);
    ivPosterImage = view.findViewById(R.id.ivPostImage);
    progressBar = view.findViewById(R.id.pbLoading);

    btnUpload.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        launchCamera();
      }
    });

    btnSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String description = etDescription.getText().toString();
        if (description.isEmpty()) {
          Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT)
               .show();
          return;
        }
        if(photoFile == null || ivPosterImage.getDrawable() == null){
          Toast.makeText(getContext(), "No Image taken", Toast.LENGTH_SHORT)
               .show();
          return;
        }
        ParseUser currentUser = ParseUser.getCurrentUser();
        Date createdTime = ParseUser.getCurrentUser().getCreatedAt();
        savePost(description, currentUser, photoFile);
        progressBar.setVisibility(ProgressBar.VISIBLE);

      }
    });


  }

  private void launchCamera() {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // Create a File reference for future access
    photoFile = getPhotoFileUri(photoFileName);

    // wrap File object into a content provider
    // required for API >= 24
    // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
    Uri fileProvider =
        FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
    // So as long as the result is not null, it's safe to use the intent.
    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
      // Start the image capture intent to take photo
      startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        // by this point we have the camera photo on disk
        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        // RESIZE BITMAP, see section below
        // Load the taken image into a preview
        etDescription.setVisibility(View.VISIBLE);
        btnUpload.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.VISIBLE);
        ivPosterImage.setImageBitmap(takenImage);
      } else { // Result was a failure
        Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
      }
    }

  }

  private File getPhotoFileUri(String photoFileName) {

    File mediaStorageDir =
        new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

    // Create the storage directory if it does not exist
    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
      Log.d(TAG, "failed to create directory");
    }

    // Return the file target for the photo based on filename
    File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);

    return file;
  }

  private void savePost(String description, ParseUser currentUser, File photoFile) {
    Post post = new Post();
    post.setDescription(description);
    post.setUser(currentUser);
    post.setImage(new ParseFile(photoFile));
    post.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e != null) {
          Log.i("MainActivity", "Post not saved", e);
          Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
        }

        Log.i("MainActivity", "Post saved!!!", e);
        // clearing textview and imageview
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        etDescription.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
        ivPosterImage.setImageResource(0);
      }
    });
  }


}