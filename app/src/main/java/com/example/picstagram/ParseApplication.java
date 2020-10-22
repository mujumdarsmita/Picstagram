package com.example.picstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    // Register your parse models
    ParseObject.registerSubclass(Post.class);

    Parse.initialize(new Parse.Configuration.Builder(this)
                         .applicationId("hFjyFWiiOwhkNqUKseSaoK4ee4Dbiw4ASzFGWo47")
                         .clientKey("sWjTTU8jvkqB8fYbllrvtFV36it3ZBypu35pqC9D")
                         .server("https://parseapi.back4app.com")
                         .build()
    );
  }
}
