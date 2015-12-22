package com.group10.lazymanscooking;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * Created by Stefan on 2015-12-18.
 */
public class DatabaseHelper {

    protected void onCreate() {
        //Parse
        Parse.initialize(this, "PuPGePBSCLlAsCqISYYavaMilT8kjxeTSKVkdL6H", "7UFaGvJYlQrRdQA9LQN0g8kPWt8WcCaAPuw3HrAy");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public Object SelectUserFromDatabase(String id){
        final Object[] result = new Object[1];
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    result[0] = object;
                } else {
                    return;
                    System.out.println("Er is iets fout gegaan");
                }
            }
        });
    }
}
