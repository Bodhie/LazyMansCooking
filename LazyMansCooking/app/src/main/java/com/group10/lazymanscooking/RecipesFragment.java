package com.group10.lazymanscooking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment {
    View rootView;
    ListView listView;
    ArrayList<Recipe> recipes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_recipes, container, false);


        listView = (ListView) rootView.findViewById(R.id.recipesListView);
        recipes = new ArrayList<>();
        final ArrayAdapter<Recipe> arrayAdapter = new ArrayAdapter<Recipe>(getActivity(), android.R.layout.simple_list_item_1, recipes);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject recipe : objects) {
                        Recipe mrecipe = new Recipe(recipe.getString("id"), recipe.getString("title"));
                        recipes.add(mrecipe);
                    }
                    listView.setAdapter(arrayAdapter);
                } else {
                    Toast.makeText(getActivity(), "Something went wrong while getting the recipes.", Toast.LENGTH_LONG).show();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                //Change fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.pager, new RecipeFragment());
                ft.commit();
//                Intent i = new Intent(RecipeActivity.this, RecipeActivity.class);
//                Recipe passrecipe = (recipes.get(position));
//                i.putExtra("recipe", passrecipe);
//                startActivity(i);
            }
        });
        return rootView;
    }
}
