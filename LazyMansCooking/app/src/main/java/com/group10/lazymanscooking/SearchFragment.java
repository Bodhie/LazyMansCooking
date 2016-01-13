package com.group10.lazymanscooking;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
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
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anjelo on 13-Jan-16.
 */
public class SearchFragment extends Fragment
{
    View rootView;
    ListView listView;
    ArrayList<IngredientCategory> categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_search, container, false);
        System.out.println("test searchfragment opened");


        listView = (ListView) rootView.findViewById(R.id.lvCategories);
        categories = new ArrayList<>();
        final ArrayAdapter<IngredientCategory> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("IngredientCategory");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject category : objects) {
                        IngredientCategory mcategory = new IngredientCategory(category.getString("id"), category.getString("title"));
                        categories.add(mcategory);
                    }
                    listView.setAdapter(arrayAdapter);
                } else {
                    Toast.makeText(getActivity(), "Something went wrong while getting the categories.", Toast.LENGTH_LONG).show();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                //Change fragment
//                RecipeFragment recipedetails = new RecipeFragment();
//                Recipe passrecipe = (recipes.get(position));
//                Bundle args = new Bundle();
//                args.putSerializable("recipe", passrecipe);
//                recipedetails.setArguments(args);
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.pager, recipedetails);
//                ft.show(recipedetails);
//                ft.commit();
            }
        });
        return rootView;
    }

}