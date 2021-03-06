package com.example.picstagram;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {

  Button btnLogout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    ActionBar actionBar = getSupportActionBar(); // or getActionBar();
    // getSupportActionBar().setTitle("My new title"); // set the top title
    //String title = actionBar.getTitle().toString(); // get the title
    actionBar.hide(); // or even hide the actionbar
    btnLogout = findViewById(R.id.btnLogout);
    btnLogout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ParseUser.logOut();
        goLoginActivity();
      }
    });


  }

  private void goLoginActivity() {
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
  }
}