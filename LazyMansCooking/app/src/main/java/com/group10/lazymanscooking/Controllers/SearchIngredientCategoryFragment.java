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
import android.widget.TextView;
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
    ListView categorieslistView;
    ListView ingredientslistView;
    ArrayList<IngredientCategory> categories = new ArrayList<>();
    ArrayList<Ingredient> chosenIngredients = new ArrayList<>();
    Bundle data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_search, container, false);
        categorieslistView = (ListView) rootView.findViewById(R.id.lvCategories);
        ingredientslistView = (ListView) rootView.findViewById(R.id.lvChosenIngredients);
        categories = new ArrayList<>();
        getDataFromFragment();
        initIngredients();
        initCatories();


        return rootView;
    }
    private void getDataFromFragment()
    {
            data = getArguments();
    }

    private void initIngredients()
    {
        if(chosenIngredients.size() == 0)
        {
            TextView tvEmpty = (TextView) rootView.findViewById(R.id.search_empty);
            ingredientslistView.setEmptyView(tvEmpty);
        }
        if(data != null)
        {
            chosenIngredients = (ArrayList)data.getSerializable("ingredientlist");
        }
        ArrayAdapter<Ingredient> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, chosenIngredients);
        ingredientslistView.setAdapter(arrayAdapter);
    }
    private void initCatories()
    {
        final ArrayAdapter<IngredientCategory> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("IngredientCategory");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject category : objects) {
                        IngredientCategory mcategory = new IngredientCategory(category.getString("id"), category.getString("name"));
                        categories.add(mcategory);
                    }
                    categorieslistView.setAdapter(arrayAdapter);
                } else {
                    Toast.makeText(getActivity(), "Something went wrong while getting the categories.", Toast.LENGTH_LONG).show();
                }
            }
        });
        categorieslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IngredientCategory clickedCategory = (IngredientCategory) parent.getItemAtPosition(position);
                System.out.println(clickedCategory);
                SearchIngredientFragment ingredientsByCategory = new SearchIngredientFragment();
                Bundle args = new Bundle();
                args.putSerializable("ingredientlist",chosenIngredients);
                args.putSerializable("category", clickedCategory);
                ingredientsByCategory.setArguments(args);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.pager, ingredientsByCategory);
                ft.show(ingredientsByCategory);
                ft.commit();
            }
        });
    }
}