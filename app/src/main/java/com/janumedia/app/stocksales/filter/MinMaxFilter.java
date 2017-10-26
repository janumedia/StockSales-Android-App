/*
 * MinMaxFilter.java
 * StokBarang
 *
 * Created by I Nengah Januartha on 10/23/17 10:45 AM
 * Copyright (c) 2017 I Nengah Januartha. All right reserved.
 *
 * Last modified 10/23/17 10:45 AM
 */

package com.janumedia.app.stocksales.filter;

import android.text.InputFilter;
import android.text.Spanned;

public class MinMaxFilter implements InputFilter {

    private int minInt, maxInt;

    public MinMaxFilter(int minValue, int maxValue) {
        this.minInt = minValue;
        this.maxInt = maxValue;
    }

    public MinMaxFilter(String minValue, String maxValue) {
        this.minInt = Integer.parseInt(minValue);
        this.maxInt = Integer.parseInt(maxValue);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned spanned, int dStart, int dEnd) {
        try {
            int input = Integer.parseInt(spanned.toString() + source.toString());
            if (isInRange(minInt, maxInt, input))
                return null;
        } catch (NumberFormatException e) { }
        return "";
    }

    private boolean isInRange(int a, int b, int c){
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
