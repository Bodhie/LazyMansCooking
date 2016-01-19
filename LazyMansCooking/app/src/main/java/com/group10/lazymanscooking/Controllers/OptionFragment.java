package com.group10.lazymanscooking.Controllers;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.group10.lazymanscooking.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Stefan on 2016-01-08.
 */
public class OptionFragment extends Fragment implements View.OnClickListener {
    View rootView;
    public static final String PREFS_NAME = "MyOptions";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_option, container, false);

        // Add a listener to the button to handle it by this class
        Button b = (Button) rootView.findViewById(R.id.btnSave);
        b.setOnClickListener(this);

        // Get the prefs of the current user
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Boolean restoredPref = prefs.getBoolean("recipeLocal", false);
        Boolean restoredPref2 = prefs.getBoolean("autoLogin", false);
        if (restoredPref != null)
        {
            if(restoredPref){
                CheckBox radioLocal = (CheckBox) rootView.findViewById(R.id.radioLocal);
                radioLocal.setChecked(true);
            }
        }
        if (restoredPref2 != null)
        {
            if(restoredPref2){
                CheckBox radioLogin = (CheckBox) rootView.findViewById(R.id.radioLogin);
                radioLogin.setChecked(true);
            }
        }

        return rootView;
    }

    public void saveOption(View v) {
        // Set the prefs of the current user.
        CheckBox radioLocal = (CheckBox) rootView.findViewById(R.id.radioLocal);
        CheckBox autoLogin = (CheckBox) rootView.findViewById(R.id.radioLogin);
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("recipeLocal", radioLocal.isChecked());
        editor.putBoolean("autoLogin", autoLogin.isChecked());
        if(radioLocal.isChecked()){
            ParseUser currentUser = ParseUser.getCurrentUser();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
            query.whereEqualTo("creatorId", currentUser.getObjectId());
            try {
                List<ParseObject> objects = query.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
        } else {
            //Unpin
        }
        editor.apply();
        Toast.makeText(getActivity(), "Options saved", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                saveOption(v);
                break;
        }
    }
}