/*
 * CustomFragment.java
 * StokBarang
 *
 * Created by I Nengah Januartha on 10/23/17 3:23 PM
 * Copyright (c) 2017 I Nengah Januartha. All right reserved.
 *
 * Last modified 10/23/17 3:23 PM
 */

package com.janumedia.app.stocksales;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.janumedia.app.stocksales.controller.DBaseController;

public class CustomFragment extends Fragment {

    private static final String TAG_DEBUG = "DEBUG";

    protected View rootView;
    protected DBaseController dBaseController;

    private LinearLayout noLayout;

    public CustomFragment() {

    }

    /**
     * This method must called inside CreateView method and after initial rootView
     */
    protected void init(){

        dBaseController = new DBaseController(this.getContext(), null, null, 1);

        noLayout = (LinearLayout) rootView.findViewById(R.id.no_stock);
        if(noLayout != null) {
            int visibility = dBaseController.hasRecordData() ? View.GONE : View.VISIBLE;
            noLayout.setVisibility(visibility);
            //special situation for CheckoutFragment
            //because it's using ScrollView
            if(visibility == View.GONE && getClass().getName().equals(CheckoutFragment.class.getName())) {
                rootView.findViewById(R.id.layout_container).setVisibility(View.VISIBLE);
            }
        }
        //log("has record: " + dBaseController.hasRecordData());

        onTapOutside();
    }

    /**
     * This method to load dataBase with WHERE selection and GROUP BY
     * @param spinner AppCompactSpinner target where return data from dataBase will rendered
     * @param whereQueryArgs WHERE selection arguments
     * @param add If true will add <b>Add New</b>item
     * @return ArrayAdapter
     * @see ArrayAdapter
     */
    protected ArrayAdapter<String> loadGrupBy(AppCompatSpinner spinner, String[] whereQueryArgs, boolean add){

        String key = DBaseController.COLUMN_TYPE;
        String whereQuery = null;
        switch (spinner.getId()){
            case R.id.spinner_type:
                //for insertFragment only! check for remove button
                AppCompatButton removeBtn = (AppCompatButton) rootView.findViewById(R.id.button_remove);
                if(removeBtn != null) removeBtn.setVisibility(dBaseController.hasRecordData() ? View.VISIBLE : View.GONE);

                key = DBaseController.COLUMN_TYPE;
                break;
            case R.id.spinner_name:
                key = DBaseController.COLUMN_NAME;
                whereQuery = DBaseController.COLUMN_TYPE + " = ?";
                break;
            case R.id.spinner_size:
                key = DBaseController.COLUMN_SIZE;
                whereQuery = DBaseController.COLUMN_TYPE + " = ? AND " + DBaseController.COLUMN_NAME + " = ?";
                break;
            case R.id.spinner_theme:
                key = DBaseController.COLUMN_THEME;
                whereQuery = DBaseController.COLUMN_TYPE + " = ? AND " + DBaseController.COLUMN_NAME + " = ? AND " +
                             DBaseController.COLUMN_SIZE + " = ?";
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_item,
                whereQueryArgs == null ? dBaseController.loadGrupBy(key, add) :
                                         dBaseController.loadGrupBy(key, whereQuery, whereQueryArgs, add));

        spinner.setAdapter(adapter);

        return adapter;
    }

    /**
     * This method to load dataBase with WHERE selection and GROUP BY
     * @param spinner AppCompactSpinner target where return data from dataBase will rendered
     * @param input AppCompactEditText which hide after data loaded
     * @param whereQueryArgs WHERE selection arguments
     * @param add If true will add <b>Add New</b>item
     * @return ArrayAdapter
     * @see ArrayAdapter
     */
    protected ArrayAdapter<String> loadGrupBy(AppCompatSpinner spinner, AppCompatEditText input, String[] whereQueryArgs, boolean add){

        String currentStr = null;
        if(spinner.getCount() > 0) {
            currentStr = (input.getText().toString().length() > 0) ? input.getText().toString() : spinner.getSelectedItem().toString();
            //avoid spinner listener
            spinner.setOnItemSelectedListener(null);
        }

        ArrayAdapter<String> adapter = loadGrupBy(spinner, whereQueryArgs, add);

        if(currentStr != null)
        {
            int targetPosition = adapter.getPosition(currentStr);
            //re-register spinner listener
            spinner.setOnItemSelectedListener(spinnerItemListener());
            spinner.setSelection(targetPosition);
        }

        spinner.setVisibility(View.VISIBLE);
        ((LinearLayout) input.getParent()).setVisibility(View.GONE);

        return adapter;
    }

    /**
     * This method for AppCompatSpinner.setOnItemSelectedListener registration
     * @return OnItemSelectedListener
     * @see AdapterView.OnItemSelectedListener
     */
    protected AdapterView.OnItemSelectedListener spinnerItemListener()
    {
        return  new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int positon, long id) {
                AppCompatSpinner sp = (AppCompatSpinner) view.getParent();
                onSpinnerItemListener((AppCompatSpinner) view.getParent());
            };

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                log("onNothingSelected");
            };
        };
    }

    protected void onSpinnerItemListener(AppCompatSpinner spinner){
        //extend implementation on subClass
    }

    private void onTapOutside(){
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideSoftKeyboard();
                return false;
            }
        });
    }

    /**
     * This method for AppCompatEditText.setOnFocusChangeListener registration
     * @return onFocusChangeListener
     * @see AppCompatEditText.OnFocusChangeListener
     */
    protected AppCompatEditText.OnFocusChangeListener textFocusChangeListener(){
        return  new AppCompatEditText.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    hideSoftKeyboard(view);
                }
            }
        };
    }

    /**
     * This method to hide softKeyboard based on current view focused
     */
    protected void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * This method to hide softKeyboard based on view defined
     * @param view This is view targeted
     */
    protected void hideSoftKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @SuppressWarnings("deprecation")
    /**
     * This method to convert HTML text format and allowed to use in any component, such: TextView.setText(), etc.
     * @param source String
     * @return Spanned
     */
    protected Spanned stringFromHtml(String source){
        if(Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(source, 0);
        }
        return Html.fromHtml(source);
    }

    @SuppressWarnings("deprecation")
    /**
     * This method to convert HTML text format and allowed to use in any component, such: TextView.setText(), etc.
     * @param source int, use R.string
     * @return Spanned
     */
    protected Spanned stringFromHtml(int source){
        if(Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(getResources().getString(source), 0);
        }
        return Html.fromHtml(getResources().getString(source));
    }

    /**
     * This method to change ActionBar title
     * @param title String
     */
    public void setTile(String title){ this.getActivity().setTitle(title); }

    /**
     * This method to change ActionBar title
     * @param title int, use R.string value
     */
    public void setTile(int title){
        getActivity().setTitle(getResources().getString(title));
    }

    /**
     * This method to make Toast message
     * @param mesg String, message to display inside Toast
     * @see Toast
     */
    protected void toast(String mesg){
        Toast.makeText(getActivity().getApplicationContext(), mesg, Toast.LENGTH_SHORT).show();
    }

    /**
     * This methos to make Toast message
     * @param mesg Int, message to display inside Toast from R.string resource
     */
    protected void toast(int mesg){
        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(mesg), Toast.LENGTH_SHORT).show();
    }

    /**
     * This method to call Log.v() debugger
     * @param log String
     */
    protected void log(String log){
        Log.v(TAG_DEBUG, getClass().getSimpleName() + ", " + log);
    }

    /*** Interface ***/
    interface onSpinnerItemSelectedListener {
        void onSpinnerItemListener(AppCompatSpinner spinner);
    }
}
