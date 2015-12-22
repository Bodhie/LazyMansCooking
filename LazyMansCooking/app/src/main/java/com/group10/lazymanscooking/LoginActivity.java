package com.group10.lazymanscooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Stefan on 2015-12-17.
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View v){
        DatabaseHelper databaseHelper = new DatabaseHelper():
        Object result = databaseHelper.SelectUserFromDatabase("LYTjTzLE8r");
        String username = (String) result.get("username");
        String password = (String) result.get("password");
        System.out.println("Username = " + username + " and password = " + password);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
