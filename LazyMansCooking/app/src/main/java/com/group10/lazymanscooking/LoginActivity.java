package com.group10.lazymanscooking;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by Stefan on 2015-12-17.
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //parse
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            // show the signup or login screen
        }
    }

    public void login(View v){
        EditText viewUsername = (EditText) findViewById(R.id.txtUsername);
        String username = viewUsername.getText().toString();
        EditText viewPassword = (EditText) findViewById(R.id.txtPassword);
        String password = viewPassword.getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    if (e != null) {
                        Toast.makeText(LoginActivity.this, "Incorrect password or username", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "You succesfully login", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void register(View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


}
