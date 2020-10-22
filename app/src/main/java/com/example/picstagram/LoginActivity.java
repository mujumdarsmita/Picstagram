package com.example.picstagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

  EditText etUsername;
  EditText etPassword;
  Button btnLogin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    if(ParseUser.getCurrentUser()!=null){
      goMainActivity();
    }

    etUsername = findViewById(R.id.etUsername);
    etPassword = findViewById(R.id.etPassword);
    btnLogin = findViewById(R.id.btnLogin);

    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        userLogin(username, password);
      }
    });

  }

  private void userLogin(String username, String password) {
    Toast.makeText(getApplicationContext(), "user login", Toast.LENGTH_SHORT).show();
    ParseUser.logInInBackground(username, password, new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if (e != null) {
          Log.e("LoginActivity", "Issue with login", e);
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