package com.group10.lazymanscooking.Controllers;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.group10.lazymanscooking.Models.Ingredient;
import com.group10.lazymanscooking.Models.IngredientCategory;
import com.group10.lazymanscooking.Models.Recipe;
import com.group10.lazymanscooking.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anjelo on 14-Jan-16.
 */
public class SearchIngredientFragment extends Fragment {
    View rootView;
    ListView listView;
    Bundle data;
    IngredientCategory category;
    ArrayList<Ingredient> ingredients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_searchingredients, container, false);
        listView = (ListView) rootView.findViewById(R.id.lvIngredients);
        ingredients = new ArrayList<>();
        getData();
        if(data != null) {
            category = (IngredientCategory) data.getSerializable("IngredientCategory");
            final ArrayAdapter<Ingredient> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, ingredients);
            ParseQuery<ParseObject> query = new ParseQuery<>("Ingredient");
            query.addAscendingOrder("name");
            query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            for (ParseObject ingredient : objects) {
                                Ingredient addIngredient = new Ingredient(ingredient.getObjectId(), ingredient.getString("name"));
                                System.out.println(addIngredient);
                                //ingredients.add(addIngredient);
                            }
                            listView.setAdapter(arrayAdapter);
                        }
                    }
                });
        }
        return rootView;
    }


        private void getData()
    {
        data = getArguments();
    }
}
