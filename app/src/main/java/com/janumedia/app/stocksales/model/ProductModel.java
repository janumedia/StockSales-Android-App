/*
 * Product.java
 * StokBarang
 *
 * Created by I Nengah Januartha on 10/21/17 12:22 PM
 * Copyright (c) 2017 I Nengah Januartha. All right reserved.
 *
 * Last modified 10/21/17 12:22 PM
 */

package com.janumedia.app.stocksales.model;

public class ProductModel {

    private String type;
    private String name;
    private String size;
    private String theme;
    private int stock;
    private int sales;

    public ProductModel(){

    }

    public void setType(String value){ this.type = value; }
    public String getType(){
        return this.type;
    }

    public void setName(String value){ this.name = value; }
    public String getName(){ return this.name; }

    public void setSize(String value){ this.size = value; }
    public String getSize(){
        return this.size;
    }

    public void setTheme(String value){ this.theme = value; }
    public String getTheme(){ return this.theme; }

    public void setStock(int value){ this.stock = value; }
    public int getStock(){ return this.stock; }

    public void setSales(int value){ this.sales = value; }
    public int getSales(){ return this.sales; }
}
