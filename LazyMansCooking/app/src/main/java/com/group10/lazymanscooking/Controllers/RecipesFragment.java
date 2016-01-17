package com.group10.lazymanscooking.Controllers;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.group10.lazymanscooking.Models.Ingredient;
import com.group10.lazymanscooking.Models.Recipe;
import com.group10.lazymanscooking.R;
import com.group10.lazymanscooking.RecipeFragment;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

public class RecipesFragment extends Fragment {
    View rootView;
    ListView listView;
    CustomAdapter adapter;
    ArrayList<Ingredient> chosenIngredients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_recipes, container, false);


        listView = (ListView) rootView.findViewById(R.id.recipesListView);

        Bundle args = this.getArguments();
        if(args == null) {
            CustomAdapter adapter = new CustomAdapter(getActivity(), "Recipes", "");
            listView.setAdapter(adapter);
        }
        else if (args.getSerializable("ingredientlist") != null) {
            //search recipes by chosen ingredients
            chosenIngredients = (ArrayList)args.getSerializable("ingredientlist");
            //TODO create query to search the database for recipes according to the available ingredients.


        }
        else
        {
            String search = args.getString("search", "");
            Boolean favorite = args.getBoolean("favorite", false);
            Boolean myRecipe = args.getBoolean("myRecipe", false);
            if (favorite) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                adapter = new CustomAdapter(getActivity(), "favorite", currentUser.getObjectId());
            } else if (myRecipe) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                adapter = new CustomAdapter(getActivity(), "myRecipe", currentUser.getObjectId());
            } else if (!search.isEmpty()) {
                adapter = new CustomAdapter(getActivity(), "SearchTitle", search);
            }else {
                adapter = new CustomAdapter(getActivity(), "Recipes", "");
            }
            listView.setAdapter(adapter);
        }
        setClickListener();
        return rootView;
    }

    public void setClickListener()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject item = (ParseObject) parent.getItemAtPosition(position);
                Recipe recipe = new Recipe(item.getObjectId(), item.getString("title"), item.getString("description"));

                RecipeFragment recipedetails = new RecipeFragment();
                Bundle args = new Bundle();
                args.putSerializable("recipe", recipe);
                recipedetails.setArguments(args);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.pager, recipedetails);
                ft.show(recipedetails);
                ft.commit();
            }
        });
    }
}
