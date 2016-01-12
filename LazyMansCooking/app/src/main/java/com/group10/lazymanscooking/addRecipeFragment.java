package com.group10.lazymanscooking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;

/**
 * Created by Stefan on 2016-01-08.
 */
public class addRecipeFragment extends Fragment implements View.OnClickListener {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_add_recipe, container, false);

        Button b = (Button) rootView.findViewById(R.id.btnAddRecipe);
        b.setOnClickListener(this);

        return rootView;
    }

    public void addRecipe(View v){
        EditText viewTitle = (EditText) rootView.findViewById(R.id.txtTitle);
        String title = viewTitle.getText().toString();

        ParseObject recipe = new ParseObject("Recipe");
        recipe.put("title", title);
        //TODO : Second value
        //gameScore.put("playerName", "Sean Plott");/
        recipe.saveInBackground();

        //Change fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.pager, new  RecipesFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddRecipe:
                addRecipe(v);
                break;
        }
    }
}