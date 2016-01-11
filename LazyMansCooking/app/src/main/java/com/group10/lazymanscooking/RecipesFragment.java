package com.group10.lazymanscooking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

    final ArrayList<Recipe> recipes = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_recipes, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.recipesListView);
        getRecipes();

        ArrayAdapter<Recipe> arrayAdapter = new ArrayAdapter<Recipe>(getActivity(), android.R.layout.simple_list_item_1, recipes);

        listView.setAdapter(arrayAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
//                Intent i = new Intent(RecipeActivity.this, RecipeActivity.class);
//                Recipe passrecipe = (recipes.get(position));
//                i.putExtra("recipe", passrecipe);
//                startActivity(i);
//            }
//        });
        return rootView;
    }

    public void getRecipes()
    {
//        EditText search = (EditText) rootView.findViewById(R.id.txtSearch);
//        String titel = search.getText().toString();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
//        query.whereEqualTo("titel", titel);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject recipe : objects) {
                        Recipe mrecipe = new Recipe(recipe.getString("id"), recipe.getString("title"));
                        recipes.add(mrecipe);
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong while getting the recipes.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
