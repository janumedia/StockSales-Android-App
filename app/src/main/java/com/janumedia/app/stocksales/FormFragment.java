/*
 * FormFragment.java
 * StokBarang
 *
 * Created by I Nengah Januartha on 10/23/17 5:28 PM
 * Copyright (c) 2017 I Nengah Januartha. All right reserved.
 *
 * Last modified 10/23/17 5:28 PM
 */

package com.janumedia.app.stocksales;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;

public class FormFragment extends CustomFragment implements CustomFragment.onSpinnerItemSelectedListener {

    protected AppCompatSpinner typeSpinner;
    protected AppCompatSpinner nameSpinner;
    protected AppCompatSpinner sizeSpinner;
    protected AppCompatSpinner themeSpinner;
    protected AppCompatEditText textInput;

    @Override
    protected void init(){

        super.init();

        typeSpinner = (AppCompatSpinner) rootView.findViewById(R.id.spinner_type);
        nameSpinner = (AppCompatSpinner) rootView.findViewById(R.id.spinner_name);
        sizeSpinner = (AppCompatSpinner) rootView.findViewById(R.id.spinner_size);
        themeSpinner = (AppCompatSpinner) rootView.findViewById(R.id.spinner_theme);
        textInput = (AppCompatEditText) rootView.findViewById(R.id.input_text);

        typeSpinner.setOnItemSelectedListener(spinnerItemListener());
        nameSpinner.setOnItemSelectedListener(spinnerItemListener());
        sizeSpinner.setOnItemSelectedListener(spinnerItemListener());
        themeSpinner.setOnItemSelectedListener(spinnerItemListener());

        textInput.setOnFocusChangeListener(textFocusChangeListener());
    }

    @Override
    public void onSpinnerItemListener(AppCompatSpinner spinner) {
        switch (spinner.getId()){
            case R.id.spinner_type:
                loadGrupBy(nameSpinner, new String[]{typeSpinner.getSelectedItem().toString()}, false);
                break;
            case R.id.spinner_name:
                loadGrupBy(sizeSpinner, new String[]{
                                typeSpinner.getSelectedItem().toString(),
                                nameSpinner.getSelectedItem().toString()},
                        false);
                break;
            case R.id.spinner_size:
                loadGrupBy(themeSpinner, new String[]{
                                typeSpinner.getSelectedItem().toString(),
                                nameSpinner.getSelectedItem().toString(),
                                sizeSpinner.getSelectedItem().toString()},
                        false);
                break;
            case R.id.spinner_theme:
                loadData(textInput);
                break;
        }

        if(textInput.hasFocus()) hideSoftKeyboard(textInput);
    }

    protected void  loadData(AppCompatEditText input){
        //manage in subclass
    }

    /*** Interface ***/
    interface LoadDataInterface{
        void loadData(AppCompatEditText input);
    }
}
