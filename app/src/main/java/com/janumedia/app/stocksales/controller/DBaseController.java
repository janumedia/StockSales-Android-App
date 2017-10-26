/*
 * DBaseController.java
 * StokBarang
 *
 * Created by I Nengah Januartha on 10/21/17 1:12 PM
 * Copyright (c) 2017 I Nengah Januartha. All right reserved.
 *
 * Last modified 10/21/17 1:12 PM
 */

package com.janumedia.app.stocksales.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.janumedia.app.stocksales.model.ProductModel;

import java.util.ArrayList;

public class DBaseController extends SQLiteOpenHelper {

    private static final int DBASE_VERSION = 1;
    private static final String DBASE_NAME = "products.db";
    private static final String TABLE_NAME = "products";
    private static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_THEME = "theme";
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_SALES = "sales";

    private static final String ADD_NEW = "Add New";

    private String prevOrder;

    public DBaseController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DBASE_NAME, factory, DBASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME + "( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SIZE + " TEXT, " +
                COLUMN_THEME + " TEXT, " +
                COLUMN_STOCK + " INTEGER, " +
                COLUMN_SALES + " INTEGER" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
    }

    /**
     * This method to inser new record to data base
     * @param product ProductModel
     * @return boolean, true = update previous record, false = insert as new record
     */
    public boolean insert(ProductModel product){

        //boolean exists = isExists(new String[]{product.getType(), product.getName(), product.getSize(), product.getTheme()});

        String query = COLUMN_TYPE + "= ? AND " + COLUMN_NAME + " = ? AND " + COLUMN_SIZE + " = ? AND " + COLUMN_THEME + " = ?";
        String[] queryArgs = new String[]{product.getType(), product.getName(), product.getSize(), product.getTheme()};

        boolean exists = isExists(query, queryArgs);

        if(exists){

            update(product, COLUMN_STOCK);

        } else
        {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TYPE, product.getType());
            values.put(COLUMN_NAME, product.getName());
            values.put(COLUMN_SIZE, product.getSize());
            values.put(COLUMN_THEME, product.getTheme());
            values.put(COLUMN_STOCK, product.getStock());
            values.put(COLUMN_SALES, product.getSales());

            this.getWritableDatabase().insertOrThrow(TABLE_NAME, null, values);
            this.getWritableDatabase().close();
        }

        return exists;
    }

    /**
     * This method to update existing record to column targeted with new product data
     * @param product ProductModel, new product data
     * @param column String, column name
     */
    public void update(ProductModel product, String column){

        String query = COLUMN_TYPE + "= ? AND " + COLUMN_NAME + " = ? AND " + COLUMN_SIZE + " = ? AND " + COLUMN_THEME + " = ?";
        String[] queryArgs = new String[]{product.getType(), product.getName(), product.getSize(), product.getTheme()};

        String currentValue = loadData(column, query, queryArgs);

        switch (column){
            case COLUMN_STOCK:
                int newStock = Integer.parseInt(currentValue) + product.getStock();
                product.setStock(newStock);
                break;
            case COLUMN_SALES:
                int newSales = Integer.parseInt(currentValue) + product.getSales();
                product.setSales(newSales);
                break;
            default:
                return;
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, product.getType());
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_SIZE, product.getSize());
        values.put(COLUMN_THEME, product.getTheme());
        values.put(COLUMN_STOCK, product.getStock());
        values.put(COLUMN_SALES, product.getSales());

        query = COLUMN_TYPE + " = ? AND " + COLUMN_NAME + " = ? AND " +
                COLUMN_SIZE + " = ? AND " + COLUMN_THEME + " = ?";

        this.getWritableDatabase().update(TABLE_NAME, values, query,
                new String []{product.getType(), product.getName(), product.getSize(), product.getTheme()});
        this.getWritableDatabase().close();
    }

    /**
     * This method to delete existing record using WHERE selection and arguments
     * @param whereQuery String, WHERE selection string
     * @param whereQueryArgs, String[], WHERE selection arguments
     */
    public void delete(String whereQuery, String[] whereQueryArgs){
        this.getWritableDatabase().delete(TABLE_NAME, whereQuery, whereQueryArgs);
        this.getWritableDatabase().close();
    }

    /**
     * This method to load dataBase with WHERE selection and GROUP BY
     * @param columnName String, use for column target and GROUP BY key
     * @param add If true will add <b>Add New</b>item
     * @return array of String
     * @see ArrayList
     */
    public ArrayList<String> loadGrupBy(String columnName, boolean add){
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor cursor = this.getReadableDatabase().query(TABLE_NAME, new String[]{columnName}, null, null, columnName, null, null, null);
        while (cursor.moveToNext()){
            arrayList.add(cursor.getString(cursor.getColumnIndex(columnName)));
        }
        cursor.close();
        if(add) arrayList.add(ADD_NEW);
        return arrayList;
    }

    /**
     * This method to load dataBase with WHERE selection and GROUP BY
     * @param columnName String, use for column target and GROUP BY key
     * @param whereQuery String, WHERE selection string
     * @param whereQueryArgs String[], WHERE selection arguments
     * @param add If true will add <b>Add New</b>item
     * @return array of String
     * @see ArrayList
     */
    public ArrayList<String> loadGrupBy(String columnName, String whereQuery, String[] whereQueryArgs, boolean add){
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor cursor = this.getReadableDatabase().query(TABLE_NAME, new String[]{columnName},
                whereQuery, whereQueryArgs, columnName, null, null);
        while (cursor.moveToNext()){
            arrayList.add(cursor.getString(cursor.getColumnIndex(columnName)));
        }
        cursor.close();
        if(add) arrayList.add(ADD_NEW);
        return arrayList;
    }

    /**
     * This method to get records from dataBase based on WHERE columns and arguments
     * @param whereColumns, String[], WHERE columns selection
     * @param whereArgs, String[], WHERE selection arguments
     * @param orderBy, String, ORDER BY key
     * @return array of ProductModel
     * @see ArrayList
     */
    public ArrayList<ProductModel> load(String[] whereColumns, String[] whereArgs, String orderBy){
        ArrayList<ProductModel> arrayList = new ArrayList<ProductModel>();
        String query = "";
        for(int i=0, size=whereColumns.length ; i < size ; i++){
            if(i > 0) query += " AND ";
            query += whereColumns[i] + " = ?";
        }
        Cursor cursor = this.getReadableDatabase().query(TABLE_NAME, null, query, whereArgs, null, null,
                        orderBy + (orderBy == prevOrder ? " DESC" : " ASC"), null);

        while (cursor.moveToNext()){
            ProductModel product = new ProductModel();
            product.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
            product.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            product.setSize(cursor.getString(cursor.getColumnIndex(COLUMN_SIZE)));
            product.setTheme(cursor.getString(cursor.getColumnIndex(COLUMN_THEME)));
            product.setStock(cursor.getInt(cursor.getColumnIndex(COLUMN_STOCK)));
            product.setSales(cursor.getInt(cursor.getColumnIndex(COLUMN_SALES)));

            arrayList.add(product);
        }
        cursor.close();
        prevOrder = prevOrder == orderBy ? null : orderBy;
        return arrayList;
    }

    /**
     * This method to get String value from column targeted
     * @param key String, column name
     * @param whereQuery String, selection query
     * @param whereQueryArgs String[], selection query arguments
     * @return String
     */
    public String loadData(String key, String whereQuery, String[] whereQueryArgs){
        String query = "SELECT " + key + " FROM " + TABLE_NAME + " WHERE " + whereQuery + " LIMIT 1;";

        return DatabaseUtils.stringForQuery(this.getReadableDatabase(), query, whereQueryArgs);
    }

    /**
     * This method to check if it has record data in dataBase
     * @return boolean
     */
    public boolean hasRecordData(){
        return (DatabaseUtils.queryNumEntries(this.getReadableDatabase(), TABLE_NAME) > 0);
    }

    /**
     * This method to check if record exist in dataBase
     * @param whereQuery String, selection query
     * @param whereQueryArgs String[], selection query arguments
     * @return boolean
     */
    public boolean isExists (String whereQuery, String[] whereQueryArgs){

        return (DatabaseUtils.queryNumEntries(this.getReadableDatabase(),
                TABLE_NAME, whereQuery, whereQueryArgs) > 0);
    }
}
