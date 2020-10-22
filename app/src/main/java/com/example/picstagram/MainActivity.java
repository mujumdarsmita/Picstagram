package com.example.picstagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  EditText etDescription;
  Button btnUpload;
  Button btnSubmit;
  ImageView ivPosterImage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    etDescription = findViewById(R.id.etDescription);
    btnUpload = findViewById(R.id.btnUpload);
    btnSubmit = findViewById(R.id.btnSubmit);
    ivPosterImage = findViewById(R.id.ivPostimage);

    queryPosts();
  }

  private void queryPosts() {
    // Specify which class to query
    final ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
    query.include(Post.KEY_USER);
    query.findInBackground(new FindCallback<Post>() {
      @Override
      public void done(List<Post> posts, ParseException e) {
        if(e!=null){
          Log.e("MainActivity","Issue with getting posts");
        }

        for(Post post : posts){
          Log.i("MainActivity",
                "Post received" + post.getDescription() + "username:" + post.getUser().getUsername());
        }
      }
    });
  }
}