package com.group10.lazymanscooking.Controllers;

import android.app.Application;

import com.group10.lazymanscooking.R;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by Stefan on 2016-01-11.
 */
public class DatabaseHelper extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Inistialize the parse database
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));
        // Initialize the facebook plugin
        ParseFacebookUtils.initialize(this);
    }
}
