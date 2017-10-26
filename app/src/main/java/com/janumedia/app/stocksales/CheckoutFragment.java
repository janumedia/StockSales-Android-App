/*
 * StokBarangFragment.java
 * StokBarang
 *
 * Created by I Nengah Januartha on 10/18/17 1:43 PM
 * Copyright (c) 2017 I Nengah Januartha. All right reserved.
 *
 * Last modified 10/18/17 1:43 PM
 */

package com.janumedia.app.stocksales;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.janumedia.app.stocksales.controller.DBaseController;
import com.janumedia.app.stocksales.filter.MinMaxFilter;
import com.janumedia.app.stocksales.model.ProductModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutFragment extends FormFragment implements FormFragment.LoadDataInterface {

    private AppCompatTextView currentValueTxt;
    private ProductModel currentProduct;

    public CheckoutFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_checkout, container, false);

        setTile(R.string.title_checkout);

        currentProduct = new ProductModel();

        init();

        currentValueTxt = (AppCompatTextView) rootView.findViewById(R.id.text_current_value);

        loadGrupBy(typeSpinner, null, false);

        return rootView;
    }

    @Override
    public void loadData(AppCompatEditText input){

        currentProduct.setType(typeSpinner.getSelectedItem().toString());
        currentProduct.setName(nameSpinner.getSelectedItem().toString());
        currentProduct.setSize(sizeSpinner.getSelectedItem().toString());
        currentProduct.setTheme(themeSpinner.getSelectedItem().toString());

        String key = DBaseController.COLUMN_TYPE;
        String whereQuery = null;
        String[] whereQueryArgs = null;
        switch (input.getId()){
            case R.id.input_text:
                key = DBaseController.COLUMN_STOCK;
                whereQuery = DBaseController.COLUMN_TYPE + " = ? AND " +
                             DBaseController.COLUMN_NAME + " = ? AND " +
                             DBaseController.COLUMN_SIZE + " = ? AND " +
                             DBaseController.COLUMN_THEME + " = ?";
                whereQueryArgs = new String[]{currentProduct.getType(), currentProduct.getName(),
                                              currentProduct.getSize(), currentProduct.getTheme()};
                break;
        }
        String stock = dBaseController.loadData(key, whereQuery, whereQueryArgs);
        currentProduct.setStock(Integer.parseInt(stock));
        currentValueTxt.setText(stringFromHtml("of &nbsp;&nbsp;<b>" + stock + "</b>"));
        input.setText("");
        input.setFilters(new InputFilter[]{ new MinMaxFilter(0, currentProduct.getStock()) });
    }

    public void onCheckout(){

        String salesStr = textInput.getText().toString();
        final int salesInt = salesStr.length() > 0 ? Integer.parseInt(salesStr) : 0;

        DialogInterface.OnClickListener alertClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {
                switch (choice){
                    case DialogInterface.BUTTON_POSITIVE:

                        currentProduct.setType(typeSpinner.getSelectedItem().toString());
                        currentProduct.setName(nameSpinner.getSelectedItem().toString());
                        currentProduct.setSize(sizeSpinner.getSelectedItem().toString());
                        currentProduct.setTheme(themeSpinner.getSelectedItem().toString());
                        currentProduct.setSales(salesInt);
                        currentProduct.setStock(currentProduct.getStock() - currentProduct.getSales());

                        dBaseController.update(currentProduct, DBaseController.COLUMN_SALES);

                        textInput.setText("");
                        textInput.setFilters(new InputFilter[]{ new MinMaxFilter(0, currentProduct.getStock()) });

                        currentValueTxt.setText(stringFromHtml("of &nbsp;&nbsp;<b>" + currentProduct.getStock() + "</b>"));

                        toast(R.string.stock_checkout);
                        break;
                }
            }
        };

        AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());

        alert.setMessage(stringFromHtml("Are your sure checkout <b>" + salesInt +
                (salesInt > 1 ? " items" : " item") + "</b> from selected stock?"))
                .setPositiveButton("YES", alertClickListener)
                .setNegativeButton("NO", alertClickListener).show();

    }
}
