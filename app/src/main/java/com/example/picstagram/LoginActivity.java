package com.example.picstagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

  EditText etUsername;
  EditText etPassword;
  Button btnLogin;
  Button btnSignUp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ActionBar actionBar = getSupportActionBar(); // or getActionBar();
   // getSupportActionBar().setTitle("My new title"); // set the top title
    //String title = actionBar.getTitle().toString(); // get the title
    actionBar.hide(); // or even hide the actionbar


    if(ParseUser.getCurrentUser()!=null){
      goMainActivity();
    }

    etUsername = findViewById(R.id.etUsername);
    etPassword = findViewById(R.id.etPassword);
    btnLogin = findViewById(R.id.btnLogin);
    btnSignUp = findViewById(R.id.btnSignUp);

    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        userLogin(username, password);
      }
    });

    // on clicking sign up botton
    btnSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        userSignUp(username, password);
      }
    });

  }

  private void userSignUp(String username, String password) {
    Toast.makeText(getApplicationContext(), "user SignUp", Toast.LENGTH_SHORT).show();

    // creating Parser user
    ParseUser user = new ParseUser();
    user.setUsername(username);
    user.setPassword(password);
    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if(e!=null){
          Log.e("LoginActivity", "Issue with Signing up", e);
        }
        Toast.makeText(getApplicationContext(), "Sign up success", Toast.LENGTH_SHORT).show();
        goMainActivity();
      }
    });
  }

  private void userLogin(String username, String password) {
    Toast.makeText(getApplicationContext(), "user login", Toast.LENGTH_SHORT).show();
    ParseUser.logInInBackground(username, password, new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if (e != null) {
          Log.e("LoginActivity", "Issue with log in", e);
          return;
        }
        Toast.makeText(getApplicationContext(), "login success", Toast.LENGTH_SHORT).show();
        goMainActivity();
      }

    });
  }

  private void goMainActivity() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();
  }

}