/*
 * TableRowItemView.java
 * StokBarang
 *
 * Created by I Nengah Januartha on 10/19/17 1:13 PM
 * Copyright (c) 2017 I Nengah Januartha. All right reserved.
 *
 * Last modified 10/19/17 1:13 PM
 */

package com.janumedia.app.stocksales.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TableRow;

import com.janumedia.app.stocksales.R;

public class TableRowView extends TableRow {

    AppCompatTextView themeTxt;
    AppCompatTextView sizeTxt;
    AppCompatTextView stockTxt;

    public TableRowView(Context context) {
        super(context);

        initControl(context);
    }

    public TableRowView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.table_row_view, this);

        //create conversion from dp value
        int padding = this.getContext().getResources().getDimensionPixelSize(R.dimen.padding);

        themeTxt = new AppCompatTextView(this.getContext());
        themeTxt.setGravity(Gravity.CENTER);
        themeTxt.setPadding(padding, padding, padding, padding);
        themeTxt.setText("Theme");
        this.addView(themeTxt, new TableRow.LayoutParams(0, -2, 4.0f));

        sizeTxt = new AppCompatTextView(this.getContext());
        sizeTxt.setGravity(Gravity.CENTER);
        sizeTxt.setPadding(padding, padding, padding, padding);
        sizeTxt.setText("Size");
        this.addView(sizeTxt, new TableRow.LayoutParams(0, -2, 4.0f));

        stockTxt = new AppCompatTextView(this.getContext());
        stockTxt.setGravity(Gravity.CENTER);
        stockTxt.setPadding(padding, padding, padding, padding);
        stockTxt.setText("0");
        this.addView(stockTxt, new TableRow.LayoutParams(0, -2, 3.0f));
    }

    public void setTheme(String value) {
        themeTxt.setText(value);
    }

    public void setSize(String value) { sizeTxt.setText(value); }

    public void setStock(String value) {
        stockTxt.setText(value);
    }
}
