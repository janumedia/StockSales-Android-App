/*
 * MainActivity.java
 * StokBarang
 *
 * Created by I Nengah Januartha on 10/18/17 8:47 AM
 * Copyright (c) 2017 I Nengah Januartha. All right reserved.
 *
 * Last modified 10/18/17 8:47 AM
 */

package com.janumedia.app.stocksales;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.janumedia.app.stocksales.controller.DBaseController;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_HOME = "home";
    private static final String TAG_SALES = "sales";
    private static final String TAG_CHECKOUT = "checkout";
    private static final String TAG_INSERT = "insert";


    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //replace SplashTheme to default
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // render app version on nav header
        TextView verText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_header_version);
        verText.setText("ver. " + BuildConfig.VERSION_NAME);

        navigationView.setNavigationItemSelectedListener(this);
        // fire first menu
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        //navigationView.getMenu().performIdentifierAction(navigationView.getMenu().getItem(0).getItemId(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        //important to make sure item isChecked
        if(!item.isChecked()) navigationView.setCheckedItem(id);

        Fragment fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (id) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment, TAG_HOME);
                fragmentTransaction.commit();
                break;
            case R.id.nav_sales:
                fragment = new SalesFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment, TAG_SALES);
                fragmentTransaction.commit();
                break;
            case R.id.nav_checkout:
                fragment = new CheckoutFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment, TAG_CHECKOUT);
                fragmentTransaction.commit();
                break;
            case R.id.nav_insert:
                fragment = new InsertFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment, TAG_INSERT);
                fragmentTransaction.commit();
                break;
        }

        drawerLayout.closeDrawers();//GravityCompat.START);

        return true;
    }

    public void onStartInsert(View view) {
        onNavigationItemSelected(navigationView.getMenu().getItem(3));
    }

    public void onLoad(View view) {
        HomeFragment homeFragment  = (HomeFragment) getSupportFragmentManager().findFragmentByTag(TAG_HOME);
        if(homeFragment != null){
            homeFragment.onLoad(DBaseController.COLUMN_THEME);
        } else {
            SalesFragment salesFragment  = (SalesFragment) getSupportFragmentManager().findFragmentByTag(TAG_SALES);
            if(salesFragment != null) salesFragment.onLoad(DBaseController.COLUMN_THEME);;
        }
    }

    public void onShortByTheme(View view) {
        onLoad(view);
    }

    public void onShortBySize(View view) {
        HomeFragment homeFragment  = (HomeFragment) getSupportFragmentManager().findFragmentByTag(TAG_HOME);
        if(homeFragment != null){
            homeFragment.onLoad(DBaseController.COLUMN_SIZE);
        } else {
            SalesFragment salesFragment  = (SalesFragment) getSupportFragmentManager().findFragmentByTag(TAG_SALES);
            if(salesFragment != null) salesFragment.onLoad(DBaseController.COLUMN_SIZE);
        }
    }

    public void onShortByStock(View view) {
        HomeFragment homeFragment  = (HomeFragment) getSupportFragmentManager().findFragmentByTag(TAG_HOME);
        if(homeFragment != null) homeFragment.onLoad(DBaseController.COLUMN_STOCK);
    }

    public void onShortBySales(View view) {
        SalesFragment salesFragment  = (SalesFragment) getSupportFragmentManager().findFragmentByTag(TAG_SALES);
        if(salesFragment != null) salesFragment.onLoad(DBaseController.COLUMN_SALES);
    }

    public void onInsert(View view) {
        InsertFragment fragment  = (InsertFragment) getSupportFragmentManager().findFragmentByTag(TAG_INSERT);
        if(fragment != null) fragment.onInsert();
    }

    public void onDelete(View view) {
        InsertFragment fragment  = (InsertFragment) getSupportFragmentManager().findFragmentByTag(TAG_INSERT);
        if(fragment != null) fragment.onDelete();
    }

    public void onClose(View view) {
        InsertFragment fragment  = (InsertFragment) getSupportFragmentManager().findFragmentByTag(TAG_INSERT);
        if(fragment != null) fragment.onClose(view);
    }

    public void onCheckout(View view) {
        CheckoutFragment fragment  = (CheckoutFragment) getSupportFragmentManager().findFragmentByTag(TAG_CHECKOUT);
        if(fragment != null) fragment.onCheckout();
    }
}
