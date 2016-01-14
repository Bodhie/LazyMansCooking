package com.group10.lazymanscooking.Controllers;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.group10.lazymanscooking.Models.IngredientCategory;
import com.group10.lazymanscooking.R;

/**
 * Created by Anjelo on 14-Jan-16.
 */
public class SearchIngredientFragment extends Fragment {
    View rootView;
    ListView listView;
    Bundle data;
    IngredientCategory category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_search, container, false);
        listView = (ListView) rootView.findViewById(R.id.lvCategories);
        getData();
        category = (IngredientCategory)data.getSerializable("category");
        Toast.makeText(getActivity(),category.getTitle(),Toast.LENGTH_LONG);
        return rootView;
    }


        private void getData()
    {
        data = getArguments();
    }
}
