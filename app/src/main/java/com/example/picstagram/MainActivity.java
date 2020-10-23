package com.example.picstagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 39;
  public static final String TAG = "MainActivity";
  EditText etDescription;
  Button btnUpload;
  Button btnSubmit;
  ImageView ivPosterImage;
  private File photoFile;
  private String photoFileName = "photo.jpg";
  ImageView ivNewPost;
  ImageView ivProfile;
  ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    etDescription = findViewById(R.id.etDescription);
    btnUpload = findViewById(R.id.btnUpload);
    btnSubmit = findViewById(R.id.btnSubmit);
    ivPosterImage = findViewById(R.id.ivPostImage);
    ivNewPost = findViewById(R.id.ivNewPost);
    ivProfile = findViewById(R.id.ivProfile);
    progressBar = findViewById(R.id.pbLoading);

    ivProfile.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // go to profile activity
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
      }
    });

    ivNewPost.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        btnUpload.setVisibility(View.VISIBLE);
      }
    });

    //queryPosts();

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
          Toast.makeText(MainActivity.this, "Description cannot be empty", Toast.LENGTH_SHORT)
               .show();
          return;
        }
        if(photoFile == null || ivPosterImage.getDrawable() == null){
          Toast.makeText(MainActivity.this, "No Image taken", Toast.LENGTH_SHORT)
               .show();
          return;
        }
        ParseUser currentUser = ParseUser.getCurrentUser();
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
        FileProvider.getUriForFile(MainActivity.this, "com.codepath.fileprovider", photoFile);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
    // So as long as the result is not null, it's safe to use the intent.
    if (intent.resolveActivity(getPackageManager()) != null) {
      // Start the image capture intent to take photo
      startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
      }
    }

  }

  private File getPhotoFileUri(String photoFileName) {

    File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

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
          Toast.makeText(MainActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
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

  private void queryPosts() {
    // Specify which class to query
    final ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
    query.include(Post.KEY_USER);
    query.findInBackground(new FindCallback<Post>() {
      @Override
      public void done(List<Post> posts, ParseException e) {
        if (e != null) {
          Log.e("MainActivity", "Issue with getting posts");
        }

        for (Post post : posts) {
          Log.i("MainActivity",
                "Post received" +
                post.getDescription() +
                "username:" +
                post.getUser().getUsername());
        }
      }
    });
  }
}