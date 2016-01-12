package com.group10.lazymanscooking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 2016-01-08.
 */
public class addRecipeFragment extends Fragment implements View.OnClickListener {
    View rootView;
    List<String> ingredients;
    ListView pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_add_recipe, container, false);

        Button b = (Button) rootView.findViewById(R.id.btnAddRecipe);
        b.setOnClickListener(this);

        //Set ingredients
        pager = (ListView) rootView.findViewById(R.id.listViewIngredients);
        ingredients = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ingredients);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Ingredient");
        query.addAscendingOrder("name");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    for (ParseObject ingredient  : objects){
                        String name = ingredient.getString("name");
                        ingredients.add(name);
                    }
                    pager.setAdapter(adapter);
                }
            }
        });


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