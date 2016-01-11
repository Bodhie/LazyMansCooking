package com.group10.lazymanscooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Anjelo on 11-Jan-16.
 */
public class AdvancedRecipeSearchFragment extends android.support.v4.app.Fragment {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_search, container, false);

        return rootView;
    }
}
