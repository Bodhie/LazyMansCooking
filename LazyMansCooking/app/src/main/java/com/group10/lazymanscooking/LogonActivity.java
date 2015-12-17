package com.group10.lazymanscooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Stefan on 2015-12-17.
 */
public class LogonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
    }

    public void login(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
