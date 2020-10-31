package com.example.picstagram;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Movie;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ui.widget.ParseImageView;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostDetailActivity extends AppCompatActivity {

  Post post;
  TextView tvName;
  TextView tvDescription;
  ParseImageView ivPost;
  TextView tvCreatedTime;
  ImageView ivComment;
  ParseImageView ivProfilePicture;
  EditText etComment;
  Button btnPost;
  TextView tvComments;
  public static final String KEY_PROFILE_IMAGE = "profilePicture";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_post_detail);
    ActionBar actionBar = getSupportActionBar(); // or getActionBar();
    actionBar.hide(); // or even hide the actionbar

    post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
    tvName = findViewById(R.id.tvName);
    tvDescription = findViewById(R.id.tvDescription);
    ivPost = findViewById(R.id.ivPost);
    tvCreatedTime = findViewById(R.id.tvCreatedTime);
    ivProfilePicture = findViewById(R.id.ivProfilePicture);
    ivComment = findViewById(R.id.ivComment);
    etComment = findViewById(R.id.etComment);
    tvComments = findViewById(R.id.tvComments);


    tvName.setText(post.getUser().getUsername());
    tvDescription.setText(post.getDescription());
    ParseFile image = post.getImage();
    if (image != null) {
      ivPost.setParseFile(post.getImage());
      ivPost.loadInBackground();
    }

    ParseFile profileImage = post.getUser().getParseFile(KEY_PROFILE_IMAGE);
    if (profileImage != null) {
      ivProfilePicture.setParseFile(profileImage);
      ivProfilePicture.loadInBackground();
    }

    Date date = post.getUser().getCreatedAt();
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    String createdTime = df.format(date);
    tvCreatedTime.setText(createdTime);

//    ivComment.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        etComment.setVisibility(View.VISIBLE);
//        btnPost.setVisibility(View.VISIBLE);
//      }
//    });

//    btnPost.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        String comment = etComment.getText().toString();
//        etComment.setVisibility(View.GONE);
//        btnPost.setVisibility(View.GONE);
//        tvComments.setVisibility(View.VISIBLE);
//        tvComments.setText(comment);
//      }
//    });


  }
}