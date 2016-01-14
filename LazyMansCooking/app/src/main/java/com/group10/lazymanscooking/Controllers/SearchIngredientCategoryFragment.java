package com.group10.lazymanscooking.Controllers;

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

import com.group10.lazymanscooking.Models.Ingredient;
import com.group10.lazymanscooking.Models.IngredientCategory;
import com.group10.lazymanscooking.Models.Recipe;
import com.group10.lazymanscooking.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anjelo on 13-Jan-16.
 */
public class SearchIngredientCategoryFragment extends Fragment
{
    View rootView;
    ListView listView;
    ArrayList<IngredientCategory> categories;
    ArrayList<Ingredient> chosenIngredients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_search, container, false);
        listView = (ListView) rootView.findViewById(R.id.lvCategories);
        categories = new ArrayList<>();
        final ArrayAdapter<IngredientCategory> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("IngredientCategory");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject category : objects) {
                        IngredientCategory mcategory = new IngredientCategory(category.getString("id"), category.getString("name"));
                        categories.add(mcategory);
                    }
                    listView.setAdapter(arrayAdapter);
                } else {
                    Toast.makeText(getActivity(), "Something went wrong while getting the categories.", Toast.LENGTH_LONG).show();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IngredientCategory clickedCategory = (IngredientCategory)parent.getItemAtPosition(position);
                SearchIngredientFragment ingredientsByCategory = new SearchIngredientFragment();
                Bundle args = new Bundle();
                args.putSerializable("category", clickedCategory);
                ingredientsByCategory.setArguments(args);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.pager, ingredientsByCategory);
                ft.show(ingredientsByCategory);
                ft.commit();
            }
        });
        return rootView;
    }

}