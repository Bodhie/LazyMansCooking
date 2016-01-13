package com.group10.lazymanscooking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Stefan on 2016-01-08.
 */
public class RecipeFragment extends Fragment {
    View rootView;
    Recipe recipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_recipe, container, false);
        TextView tvRecipeTitle = (TextView)rootView.findViewById(R.id.tvRecipeTitle);
        TextView tvRecipeDescription= (TextView)rootView.findViewById(R.id.tvRecipeDescription);
        final ImageView ivRecipe = (ImageView) rootView.findViewById(R.id.image_header);

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

                    } else {
                        Toast.makeText(getActivity(), "something wong" + e.toString(), Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
        return rootView;
    }

    public void addFavorite(View v){
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseObject favorite = new ParseObject("Favorite");
        favorite.put("user_id", currentUser.getObjectId());
        favorite.put("recipe_id", recipe.getObjectId());
        favorite.saveInBackground();

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFavorite:
                addFavorite(v);
                break;
        }
    }
}