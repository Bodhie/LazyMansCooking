package com.group10.lazymanscooking.Controllers;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.group10.lazymanscooking.Models.Ingredient;
import com.group10.lazymanscooking.Models.IngredientCategory;
import com.group10.lazymanscooking.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 2016-01-08.
 */
public class addRecipeFragment extends Fragment implements View.OnClickListener {
    View rootView;
    ArrayList<Ingredient> availableIngredients;
    IngredientCategory chosenCategory;
    ArrayList<Ingredient> chosenIngredients = new ArrayList<>();
    ListView listViewChosenIngredients;
    ListView listViewOptions;
    TextView tvEmpty;
    String currentLongitude;
    String currentLatitude;
    private static final int SELECT_PHOTO = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_add_recipe, container, false);

        Button b = (Button) rootView.findViewById(R.id.btnAddRecipe);
        b.setOnClickListener(this);
        setChosenIngredients();
        setOptions();
        //Image setter
        Button pickImage = (Button) rootView.findViewById(R.id.btn_pick);
        pickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void setChosenIngredients()
    {
        listViewChosenIngredients = (ListView) rootView.findViewById(R.id.listViewChosenIngredients);
        final ArrayAdapter<Ingredient> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, chosenIngredients);
        listViewChosenIngredients.setAdapter(arrayAdapter);
        tvEmpty = (TextView) rootView.findViewById(R.id.search_empty);
        listViewChosenIngredients.setEmptyView(tvEmpty);

    }
    public void setOptions() {
        //Set categories
        String QueryInput;
        final ArrayList<Ingredient> availableIngredients = new ArrayList<>();
        final ArrayList<IngredientCategory> availableCategories = new ArrayList<>();
        if(chosenCategory != null)
        {
            QueryInput = "Ingredient";
        }
        else
        {
            QueryInput = "IngredientCategory";
        }
        listViewOptions = (ListView) rootView.findViewById(R.id.listViewDisplayOptions);

        final TextView tvEmptyOptions = (TextView) rootView.findViewById(R.id.search_emptyOptions);
        final ArrayAdapter<Ingredient> arrayAdapterIngredients = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, availableIngredients);
        final ArrayAdapter<IngredientCategory> arrayAdapterCategories = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, availableCategories);

        ParseQuery<ParseObject> query = new ParseQuery<>(QueryInput);
        query.addAscendingOrder("name");
        if(chosenCategory != null)
        {
            query.whereEqualTo("IngredientCategory_id", chosenCategory.getobjectId());
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject output : objects) {
                        if(chosenCategory != null)
                        {
                            System.out.println(chosenCategory.getTitle());
                            Ingredient ingredient = new Ingredient(output.getObjectId(),output.getString("name"));
                            availableIngredients.add(ingredient);
                        }
                        else {
                            IngredientCategory category = new IngredientCategory(output.getObjectId(), output.getString("name"));
                            availableCategories.add(category);
                        }
                    }
                    if(chosenCategory != null)
                    {
                        listViewOptions.setAdapter(arrayAdapterIngredients);
                    }
                    else
                    {
                        listViewOptions.setAdapter(arrayAdapterCategories);
                    }
                    listViewOptions.setEmptyView(tvEmptyOptions);
                }
            }
        });
        listViewOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean ingredientExists = false;
                if(chosenCategory != null)
                {
                    Ingredient clickedIngredient = (Ingredient) parent.getItemAtPosition(position);
                    for(Ingredient ingredient : chosenIngredients)
                    {
                        if(clickedIngredient.getobjectId().equals(ingredient.getobjectId())) {
                            ingredientExists = true;
                        }
                    }
                    if(ingredientExists)
                    {
                        Toast.makeText(getActivity(),"this ingredient is already added to the chosen ingredients",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        chosenIngredients.add(clickedIngredient);
                    }
                    setChosenIngredients();
                    chosenCategory = null;
                }
                else
                {
                    chosenCategory = (IngredientCategory) parent.getItemAtPosition(position);
                }
                setOptions();
            }
        });
    }
    public void addRecipe(View v){
        //Insert into recipe
        EditText viewTitle = (EditText) rootView.findViewById(R.id.txtTitle);
        String title = viewTitle.getText().toString();
        EditText viewDescription = (EditText) rootView.findViewById(R.id.txtDescription);
        String description = viewDescription.getText().toString();
        ImageView viewImage = (ImageView) rootView.findViewById(R.id.imageView);
        Bitmap bitmap = ((BitmapDrawable) viewImage.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        getCurrentLocation();
        byte[] image = stream.toByteArray();
        ParseFile file = new ParseFile("image.png", image);
        ParseUser currentUser = ParseUser.getCurrentUser();

        final ParseObject recipe = new ParseObject("Recipe");
        recipe.put("title", title);
        recipe.put("description", description);
        recipe.put("image", file);
        recipe.put("latitude",currentLatitude);
        recipe.put("longitude",currentLongitude);
        recipe.put("creatorId", currentUser.getObjectId());
        //Add ingredients to recipe
        recipe.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //saved successfully
                    String objectId = recipe.getObjectId();
                    System.out.println(objectId);

                    ListView viewIngredients = (ListView) rootView.findViewById(R.id.listViewIngredients);
                    SparseBooleanArray checkedItems = viewIngredients.getCheckedItemPositions();
                    if (checkedItems != null) {
                        for (int i=0; i<checkedItems.size(); i++) {
                            if (checkedItems.valueAt(i)) {
                                Ingredient ingredient = (Ingredient) viewIngredients.getAdapter().getItem(checkedItems.keyAt(i));
                                //Ingredient ingredient = new Ingredient(item.getObjectId(), item.getString("name"));
                                System.out.println(ingredient + "was selected");
                                final ParseObject ingredientInsert = new ParseObject("RecipeIngredient");
                                ingredientInsert.put("recipeId", objectId);
                                ingredientInsert.put("ingredientId", ingredient.getobjectId());
                                ingredientInsert.saveInBackground();
                            }
                        }
                    }
                    final ParseObject ingredients = new ParseObject("Recipe");
                }

            }
        });


        //Change fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.pager, new RecipesFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void getCurrentLocation()
    {
        CustomLocationListener gps = new CustomLocationListener(getActivity());

        if(gps.canGetLocation())
        {
            currentLatitude = String.valueOf(gps.getLatitude());
            currentLongitude = String.valueOf(gps.getLongitude());
        }
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