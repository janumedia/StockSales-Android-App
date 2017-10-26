/*
 * HomeFragment.java
 * StokBarang
 *
 * Created by I Nengah Januartha on 10/18/17 1:09 PM
 * Copyright (c) 2017 I Nengah Januartha. All right reserved.
 *
 * Last modified 10/18/17 1:09 PM
 */

package com.janumedia.app.stocksales;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends TableFragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        setTile(R.string.app_name);

        init();

        return rootView;
    }
}
