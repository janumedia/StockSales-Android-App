/*
 * TambahBarangFragment.java
 * StokBarang
 *
 * Created by I Nengah Januartha on 10/18/17 1:14 PM
 * Copyright (c) 2017 I Nengah Januartha. All right reserved.
 *
 * Last modified 10/18/17 1:14 PM
 */

package com.janumedia.app.stocksales;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.janumedia.app.stocksales.controller.DBaseController;
import com.janumedia.app.stocksales.model.ProductModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class InsertFragment extends FormFragment implements CustomFragment.onSpinnerItemSelectedListener {

    private AppCompatEditText typeInput;
    private AppCompatEditText nameInput;
    private AppCompatEditText sizeInput;
    private AppCompatEditText themeInput;

    public InsertFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_insert, container, false);

        setTile(R.string.title_insert);

        init();

        typeInput = (AppCompatEditText) rootView.findViewById(R.id.input_type);
        nameInput = (AppCompatEditText) rootView.findViewById(R.id.input_name);
        sizeInput = (AppCompatEditText) rootView.findViewById(R.id.input_size);
        themeInput = (AppCompatEditText) rootView.findViewById(R.id.input_theme);

        typeInput.setOnFocusChangeListener(textFocusChangeListener());
        nameInput.setOnFocusChangeListener(textFocusChangeListener());
        sizeInput.setOnFocusChangeListener(textFocusChangeListener());
        themeInput.setOnFocusChangeListener(textFocusChangeListener());

        loadGrupBy(typeSpinner, typeInput, null, true);

        return rootView;
    }

    @Override
    public void onSpinnerItemListener(AppCompatSpinner spinner) {
        int visibility = spinner.getSelectedItemPosition() == spinner.getCount() - 1 ? View.VISIBLE : View.GONE;
        switch (spinner.getId()){
            case R.id.spinner_type:
                toggleVisibility(typeSpinner, typeInput, visibility);
                loadGrupBy(nameSpinner, nameInput,
                        new String[]{
                                typeSpinner.getSelectedItem().toString()
                        }, true);
                break;
            case R.id.spinner_name:
                toggleVisibility(nameSpinner, nameInput, visibility);
                loadGrupBy(sizeSpinner, sizeInput,
                        new String[]{
                                typeSpinner.getSelectedItem().toString(),
                                nameSpinner.getSelectedItem().toString()
                        }, true);
                break;
            case R.id.spinner_size:
                toggleVisibility(sizeSpinner, sizeInput, visibility);
                loadGrupBy(themeSpinner, themeInput,
                        new String[]{
                                typeSpinner.getSelectedItem().toString(),
                                nameSpinner.getSelectedItem().toString(),
                                sizeSpinner.getSelectedItem().toString()
                        }, true);
                break;
            case R.id.spinner_theme:
                toggleVisibility(themeSpinner, themeInput, visibility);
                loadData(textInput);
                break;
        }

        if(textInput.hasFocus()) hideSoftKeyboard(textInput);
    }

    private void toggleVisibility(AppCompatSpinner spinner, AppCompatEditText input, int visibility){

        if(spinner.getCount() <= 1) visibility = View.VISIBLE;

        spinner.setVisibility(visibility == View.GONE ? View.VISIBLE : View.GONE);
        ((LinearLayout) input.getParent()).setVisibility(spinner.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        if(spinner.getVisibility() == View.GONE) {
            spinner.setSelection(0);
            input.setText("");
        }
    }

    public void onClose(View view){
        switch (((LinearLayout)view.getParent()).getId()){
            case R.id.group_input_type:
                toggleVisibility(typeSpinner, typeInput, View.GONE);
                break;
            case R.id.group_input_size:
                toggleVisibility(sizeSpinner, sizeInput, View.GONE);
                break;
            case R.id.group_input_name:
                toggleVisibility(nameSpinner, nameInput, View.GONE);
                break;
            case R.id.group_input_theme:
                toggleVisibility(themeSpinner, themeInput, View.GONE);
                break;
        }
    }

    public void onInsert(){
        ProductModel product = new ProductModel();
        product.setType(typeSpinner.getVisibility() == View.GONE ?
                typeInput.getText().toString() : typeSpinner.getSelectedItem().toString());
        product.setName(nameSpinner.getVisibility() == View.GONE ?
                nameInput.getText().toString() : nameSpinner.getSelectedItem().toString());
        product.setSize(sizeSpinner.getVisibility() == View.GONE ?
                sizeInput.getText().toString() : sizeSpinner.getSelectedItem().toString());
        product.setTheme(themeSpinner.getVisibility() == View.GONE ?
                themeInput.getText().toString() : themeSpinner.getSelectedItem().toString());
        String stockStr = textInput.getText().toString();
        int stockInt = stockStr.length() > 0 ? Integer.parseInt(stockStr) : 0;
        product.setStock(stockInt);

        boolean isUpdateEntry = dBaseController.insert(product);

        if(isUpdateEntry){
            toast(R.string.stock_updated);
        } else {
            toast(R.string.stock_inserted);
        }

        //refresh list
        loadGrupBy(typeSpinner, nameInput, null, true);

        textInput.setText("");

        //reset scroll to top
        ((ScrollView) rootView).scrollTo(0, 0);
    }

    public void onDelete(){

        DialogInterface.OnClickListener alertClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {
                switch (choice){
                    case DialogInterface.BUTTON_POSITIVE:

                        String whereQuery = DBaseController.COLUMN_TYPE + " = ? AND " +
                                            DBaseController.COLUMN_NAME + " = ? AND " +
                                            DBaseController.COLUMN_SIZE + " = ? AND " +
                                            DBaseController.COLUMN_THEME + " = ?";
                        String[] whereQueryArgs = new String[]{
                                typeSpinner.getSelectedItem().toString(),
                                nameSpinner.getSelectedItem().toString(),
                                sizeSpinner.getSelectedItem().toString(),
                                themeSpinner.getSelectedItem().toString()
                        };

                        dBaseController.delete(whereQuery, whereQueryArgs);

                        //refresh list
                        loadGrupBy(typeSpinner, nameInput, null, true);

                        textInput.setText("");

                        //reset scroll to top
                        ((ScrollView) rootView).scrollTo(0, 0);

                        toast(R.string.stock_deleted);
                        break;
                }
            }
        };

        AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
        alert.setMessage("Are your sure delete selected stock?")
                .setPositiveButton("YES", alertClickListener)
                .setNegativeButton("NO", alertClickListener).show();

    }
}
