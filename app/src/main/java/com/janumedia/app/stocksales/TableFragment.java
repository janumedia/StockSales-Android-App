/*
 * TableFragment.java
 * StokBarang
 *
 * Created by I Nengah Januartha on 10/23/17 3:55 PM
 * Copyright (c) 2017 I Nengah Januartha. All right reserved.
 *
 * Last modified 10/23/17 3:55 PM
 */

package com.janumedia.app.stocksales;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.janumedia.app.stocksales.controller.DBaseController;
import com.janumedia.app.stocksales.model.ProductModel;
import com.janumedia.app.stocksales.view.TableRowView;

import java.util.ArrayList;

public class TableFragment extends CustomFragment {

    private static final String TAG = "TableFragment";

    private AppCompatSpinner typeSpinner;
    private AppCompatSpinner nameSpinner;
    private TableLayout tableHeader;
    private TableLayout tableLayout;
    private LinearLayout btnContainer;

    @Override
    protected void init(){
        super.init();

        tableHeader = (TableLayout) rootView.findViewById(R.id.row_header);
        tableLayout = (TableLayout) rootView.findViewById(R.id.row_container);

        typeSpinner = (AppCompatSpinner) rootView.findViewById(R.id.spinner_type);
        nameSpinner = (AppCompatSpinner) rootView.findViewById(R.id.spinner_name);
        btnContainer = (LinearLayout) rootView.findViewById(R.id.button_container);

        typeSpinner.setOnItemSelectedListener(spinnerItemListener());
        nameSpinner.setOnItemSelectedListener(spinnerItemListener());

        //log("init");

        loadGrupBy(typeSpinner, null, false);
    }

    @Override
    protected void onSpinnerItemListener(AppCompatSpinner spinner) {
        tableHeader.setVisibility(View.GONE);
        tableLayout.setVisibility(View.GONE);
        btnContainer.setVisibility(View.VISIBLE);

        switch (spinner.getId()) {
            case R.id.spinner_type:
                loadGrupBy(nameSpinner, new String[]{typeSpinner.getSelectedItem().toString()}, false);
                break;
        }
    }

    public void onLoad(String orderBy){

        // clear all views
        tableLayout.removeAllViews();

        tableHeader.setVisibility(View.VISIBLE);
        tableLayout.setVisibility(View.VISIBLE);
        btnContainer.setVisibility(View.GONE);

        String[] whereColumns = new String[]{DBaseController.COLUMN_TYPE, DBaseController.COLUMN_NAME};
        String[] whereArgs = new String[]{typeSpinner.getSelectedItem().toString(), nameSpinner.getSelectedItem().toString()};
        ArrayList<ProductModel> products = dBaseController.load(whereColumns, whereArgs, orderBy);

        for(int i=0, length = products.size() ; i < length ; i++){

            TableRowView row = new TableRowView(this.getContext());
            int color = getClass().getName().equals(HomeFragment.class.getName()) ? R.color.colorGreenLight : R.color.colorAccentLight;
            if(i%2 == 1) row.setBackgroundColor(ContextCompat.getColor(this.getContext(), color));
            row.setTheme(products.get(i).getTheme());
            row.setSize(products.get(i).getSize());
            Boolean isSales = this.getClass().getName().equals(SalesFragment.class.getName());
            row.setStock(Integer.toString(isSales ? products.get(i).getSales() : products.get(i).getStock()));
            tableLayout.addView(row);

        }

    }

}
