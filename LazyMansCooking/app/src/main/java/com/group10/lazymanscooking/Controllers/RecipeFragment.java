package com.group10.lazymanscooking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.group10.lazymanscooking.Models.Ingredient;
import com.group10.lazymanscooking.Models.Recipe;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 2016-01-08.
 */
public class RecipeFragment extends Fragment {
    View rootView;
    Recipe recipe;
    List<Ingredient> ingredients;
    ListView listView;
    Button addFavorite;
    private RatingBar ratingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_recipe, container, false);
        TextView tvRecipeTitle = (TextView)rootView.findViewById(R.id.tvRecipeTitle);
        TextView tvRecipeDescription= (TextView)rootView.findViewById(R.id.tvRecipeDescription);
        final ImageView ivRecipe = (ImageView) rootView.findViewById(R.id.image_header);
        addFavorite = (Button) rootView.findViewById(R.id.btnFavorite);

        Bundle data = getArguments();
        if(data != null) {
            recipe = (Recipe) data.getSerializable("recipe");
            tvRecipeTitle.setText(recipe.getTitle());
            tvRecipeDescription.setText(recipe.getDescription());

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
            query.whereEqualTo("objectId", recipe.getObjectId());
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (object != null) {
                        try {
                            ParseFile file = (ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {


                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        //use this bitmap as you want
                                        Toast.makeText(getActivity(), "something wight", Toast.LENGTH_LONG).show();
                                        ivRecipe.setImageBitmap(bitmap);

                                    } else {
                                        // something went wrong
                                        Toast.makeText(getActivity(), "something wong" + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getActivity(), "something wong" + e.toString(), Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

        //Set ingredients
        listView = (ListView) rootView.findViewById(R.id.listViewIngredients);
        ingredients = new ArrayList<>();
        final ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(getActivity(), android.R.layout.simple_list_item_1, ingredients);

        ParseQuery<ParseObject> recipeIngredient = ParseQuery.getQuery("RecipeIngredient");
        recipeIngredient.whereEqualTo("recipeId", recipe.getObjectId());

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Ingredient");
        query.whereMatchesKeyInQuery("objectId", "ingredientId", recipeIngredient);
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

        final ParseUser currentUser = ParseUser.getCurrentUser();



        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Favorite");
        query1.whereEqualTo("user_id", currentUser.getObjectId());
        query1.whereEqualTo("recipe_id", recipe.getObjectId());

        query1.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(final ParseObject object, ParseException e) {
                if (e == null) {
                    //object exists
                    addFavorite.setText("Remove from Favorites");
                    Toast.makeText(getActivity(), "something wight", Toast.LENGTH_LONG).show();
                    addFavorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            object.deleteInBackground();
                            addFavorite.setText("Removed from Favorites");
                        }
                    });


                } else {
                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        //object doesn't exist
                        addFavorite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ParseObject favorite = new ParseObject("Favorite");
                                favorite.put("user_id", currentUser.getObjectId());
                                favorite.put("recipe_id", recipe.getObjectId());
                                favorite.saveInBackground();
                                addFavorite.setText("Added to Favorites");
                            }
                        });


                    } else {
                        //unknown error, debug
                        Toast.makeText(getActivity(), "Errore", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //Rating system
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);

        //Check if user allready rated
        ParseQuery<ParseObject> queryRating = ParseQuery.getQuery("RecipeRating");
        queryRating.whereEqualTo("userId", currentUser.getObjectId());
        queryRating.whereEqualTo("recipeId", recipe.getObjectId());
        queryRating.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(final ParseObject object, final ParseException e) {
                if (object != null) {
                    System.out.println("Set rating");
                    String yourCurrentRating = String.valueOf(object.getInt("rating"));
                    ratingBar.setRating(Float.parseFloat(yourCurrentRating));
                }
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        int ratings = Integer.valueOf((int) rating);
                        System.out.println(ratings);
                        if (e == null) {
                            System.out.println("Update");
                            ParseObject currentRating = object;
                            currentRating.put("rating", ratings);
                            currentRating.saveInBackground();
                        } else {
                            System.out.println("Create");
                            ParseObject insertRating = new ParseObject("RecipeRating");
                            insertRating.put("userId", currentUser.getObjectId());
                            insertRating.put("recipeId", recipe.getObjectId());
                            insertRating.put("rating", ratings);
                            insertRating.saveInBackground();
                        }
                    }
                });
            }
        });
        return rootView;
    }
}