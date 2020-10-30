package com.example.picstagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.picstagram.fragments.ComposeFragment;
import com.example.picstagram.fragments.ProfileFragment;
import com.example.picstagram.fragments.TimelineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

  final FragmentManager fragmentManager = getSupportFragmentManager();
  BottomNavigationView bottomNavigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ActionBar actionBar = getSupportActionBar(); // or getActionBar();
    // getSupportActionBar().setTitle("My new title"); // set the top title
    //String title = actionBar.getTitle().toString(); // get the title
    actionBar.hide(); // or even hide the actionbar
    bottomNavigationView = findViewById(R.id.bottomNavigation);

//    ivProfile.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        // go to profile activity
//        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//        startActivity(intent);
//      }
//    });

//    ivNewPost.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        btnUpload.setVisibility(View.VISIBLE);
//      }
//    });

    //queryPosts();





    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment;
        switch (menuItem.getItemId()) {
          case R.id.action_home:
            // do something here
            //Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
            fragment = new TimelineFragment();
            break;
          case R.id.action_compose:
            // do something here
            //Toast.makeText(MainActivity.this, "compose", Toast.LENGTH_SHORT).show();
            fragment = new ComposeFragment();
            break;
          case R.id.action_profile:
            // do something here
            //Toast.makeText(MainActivity.this, "profile", Toast.LENGTH_SHORT).show();
            fragment = new ProfileFragment();
            break;
          default: return true;
        }

        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
        return true;
      }
    });

    bottomNavigationView.setSelectedItemId(R.id.action_home);
  }


}