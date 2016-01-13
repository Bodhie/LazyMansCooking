package com.group10.lazymanscooking;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Stefan on 2016-01-08.
 */
public class RecipeFragment extends Fragment {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_recipe, container, false);
        TextView tvRecipeTitle = (TextView)rootView.findViewById(R.id.tvRecipeTitle);
        TextView tvRecipeDescription = (TextView)rootView.findViewById(R.id.tvRecipeDescription);
        Bundle data = getArguments();
        if(data != null) {
            Recipe recipe = (Recipe) data.getSerializable("recipe");
            tvRecipeTitle.setText(recipe.getTitle());
            tvRecipeDescription.setText(recipe.getDescription());
        }
        return rootView;
    }
}