package com.group10.lazymanscooking.Controllers;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.group10.lazymanscooking.Models.Ingredient;
import com.group10.lazymanscooking.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
    List<Ingredient> ingredients;
    ListView listView;
    private static final int SELECT_PHOTO = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_add_recipe, container, false);

        Button b = (Button) rootView.findViewById(R.id.btnAddRecipe);
        b.setOnClickListener(this);

        //Set ingredients
        listView = (ListView) rootView.findViewById(R.id.listViewIngredients);
        ingredients = new ArrayList<>();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(getActivity(), android.R.layout.select_dialog_multichoice, ingredients);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Ingredient");
        query.addAscendingOrder("name");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject ingredient : objects) {
                        Ingredient addIngredient = new Ingredient(ingredient.getObjectId(), ingredient.getString("name"));
                        ingredients.add(addIngredient);
                    }
                    listView.setAdapter(adapter);
                }
            }
        });

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
        byte[] image = stream.toByteArray();
        ParseFile file = new ParseFile("image.png", image);

        final ParseObject recipe = new ParseObject("Recipe");
        recipe.put("title", title);
        recipe.put("description", description);
        recipe.put("image", file);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddRecipe:
                addRecipe(v);
                break;
        }
    }
}