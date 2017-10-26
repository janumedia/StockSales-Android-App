/*
 * SalesFragment.java
 * StokBarang
 *
 * Created by I Nengah Januartha on 10/23/17 2:44 PM
 * Copyright (c) 2017 I Nengah Januartha. All right reserved.
 *
 * Last modified 10/23/17 2:44 PM
 */

package com.janumedia.app.stocksales;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class SalesFragment extends TableFragment {

    public SalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_sales, container, false);

        setTile(R.string.title_sales);

        init();

        return rootView;
    }

}
