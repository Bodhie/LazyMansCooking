package com.group10.lazymanscooking;

import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
        Bundle args = this.getArguments();
        if(args == null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (ParseObject recipe : objects) {

                            Recipe mrecipe = new Recipe(recipe.getObjectId(), recipe.getString("title"),recipe.getString("description"));

                            recipes.add(mrecipe);
                        }
                        listView.setAdapter(arrayAdapter);
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong while getting the recipes.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            String search = args.getString("search", "");
            Boolean favorite = args.getBoolean("favorite", false);
            if (favorite) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                ParseQuery<ParseObject> favorites = ParseQuery.getQuery("Favorite");
                favorites.whereEqualTo("user_id", currentUser.getObjectId());


                ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
                query.whereMatchesKeyInQuery("objectId", "recipe_id", favorites);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            for (ParseObject recipe : objects) {
                                Recipe mrecipe = new Recipe(recipe.getString("id"), recipe.getString("title"),recipe.getString("description"));
                                recipes.add(mrecipe);
                            }
                            listView.setAdapter(arrayAdapter);
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong while getting the recipes.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else if (!search.isEmpty()) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
                query.whereMatches("title", "("+search+")", "i");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            for (ParseObject recipe : objects) {
                                Recipe mrecipe = new Recipe(recipe.getString("id"), recipe.getString("title"),recipe.getString("description"));
                                recipes.add(mrecipe);
                            }
                            listView.setAdapter(arrayAdapter);
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong while getting the recipes.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else {
                //Do nothing
            }
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                //Change fragment
                RecipeFragment recipedetails = new RecipeFragment();
                Recipe passrecipe = (recipes.get(position));
                Bundle args = new Bundle();
                args.putSerializable("recipe", passrecipe);
                recipedetails.setArguments(args);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.pager, recipedetails);
                ft.show(recipedetails);
                ft.commit();
            }
        });
        return rootView;
    }
}
