package com.group10.lazymanscooking.Controllers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.group10.lazymanscooking.Models.Recipe;
import com.group10.lazymanscooking.R;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;

public class CustomAdapter extends ParseQueryAdapter<ParseObject> {

	public ArrayList<Recipe> recipes = new ArrayList<>();
	public CustomAdapter(Context context, final String clause, final String value) {
		// Use the QueryFactory to construct a PQA that will only show
		// Todos marked as high-pri
		super(context, new QueryFactory<ParseObject>() {
			public ParseQuery create() {
				ParseQuery<ParseObject> query;
				if(clause.equals("favorite")){
					ParseQuery<ParseObject> favorites = ParseQuery.getQuery("Favorite");
					favorites.whereEqualTo("user_id", value);

					query = ParseQuery.getQuery("Recipe");
					query.whereMatchesKeyInQuery("objectId", "recipe_id", favorites);
				} else if(clause.equals("SearchTitle")){
					query = ParseQuery.getQuery("Recipe");
					query.whereMatches("title", "(" + value + ")", "i");
				} else{
					query = ParseQuery.getQuery("Recipe");
				}
				return query;
			}
		});
	}

	// Customize the layout by overriding getItemView
	@Override
	public View getItemView(ParseObject object, View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.listview_item, null);
		}

		super.getItemView(object, v, parent);

		// Add and download the image
		ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.icon);
		ParseFile imageFile = object.getParseFile("image");
		if (imageFile != null) {
			todoImage.setParseFile(imageFile);
			todoImage.loadInBackground();
		}

		// Add the title view
		TextView titleTextView = (TextView) v.findViewById(R.id.text1);
		titleTextView.setText(object.getString("title"));

		// Add a reminder of how long this item has been outstanding
		TextView timestampView = (TextView) v.findViewById(R.id.timestamp);
		timestampView.setText(object.getCreatedAt().toString());

		//add rating

		//Fill arraylist
		Recipe recipe = new Recipe(object.getObjectId(), object.getString("title"),object.getString("description"));
		recipes.add(recipe);

		return v;
	}

}
