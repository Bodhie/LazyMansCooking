package com.group10.lazymanscooking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class Splash extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //parse
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));
        ParseFacebookUtils.initialize(this);
       // ImageView splashImage = (ImageView) findViewById(R.id.splashImage);
        //splashImage.setImageResource(R.drawable.cooking1080);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                } else {
                    startActivity(new Intent(Splash.this, LoginActivity.class));
                }
                finish();
            }
        }, secondsDelayed * 3000);
    }
}