package com.group10.lazymanscooking.Views;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.group10.lazymanscooking.Controllers.CustomAdapter;
import com.group10.lazymanscooking.Models.Recipe;
import com.group10.lazymanscooking.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class RecipesFragment extends Fragment {
    View rootView;
    ListView listView;
    CustomAdapter adapter;

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
        else{
            String search = args.getString("search", "");
            Boolean favorite = args.getBoolean("favorite", false);
            if (favorite) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                Toast.makeText(getActivity(), "Selected favorite", Toast.LENGTH_LONG).show();
                adapter = new CustomAdapter(getActivity(), "favorite", currentUser.getObjectId());
            }
            else if (!search.isEmpty()) {
                adapter = new CustomAdapter(getActivity(), "SearchTitle", search);
            }else {
                adapter = new CustomAdapter(getActivity(), "Recipes", "");
            }
            listView.setAdapter(adapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject item = (ParseObject) parent.getItemAtPosition(position);
                Recipe recipe = new Recipe(item.getObjectId(), item.getString("title"),item.getString("description"));

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
        return rootView;
    }
}
