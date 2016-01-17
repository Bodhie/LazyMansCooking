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

        Button b = (Button) rootView.findViewById(R.id.btnSave);
        b.setOnClickListener(this);

        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        Boolean restoredPref = prefs.getBoolean("recipeLocal", false);
        if (restoredPref != null)
        {
            if(restoredPref){
                CheckBox radioLocal = (CheckBox) rootView.findViewById(R.id.radioLocal);
                radioLocal.setChecked(true);
            }
        }

        return rootView;
    }

    public void saveOption(View v) {
        CheckBox radioLocal = (CheckBox) rootView.findViewById(R.id.radioLocal);
        SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        editor.putBoolean("recipeLocal", radioLocal.isChecked());
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