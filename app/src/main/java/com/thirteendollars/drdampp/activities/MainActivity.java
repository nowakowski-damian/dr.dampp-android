package com.thirteendollars.drdampp.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.thirteendollars.drdampp.R;
import com.thirteendollars.drdampp.fragments.GUIControlFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{




    private DrawerLayout mDrawer;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        
        
        

    }

    private void initViews() {

        //init variables
        mToolbar=(Toolbar) findViewById(R.id.toolbar);
        mDrawer=(DrawerLayout) findViewById(R.id.drawer_layout);

        //init views
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_status) {

        } else if (id == R.id.nav_joystickcontrol) {
            replaceFragment(R.id.content,new GUIControlFragment(),GUIControlFragment.FRAGMENT_TAG,null);

        } else if (id == R.id.nav_voicecontrol) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void replaceFragment(int contaitnerId, Fragment fragment,String fragmentTag,String backStackTag) {

        if (backStackTag == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(contaitnerId, fragment, fragmentTag)
                    .disallowAddToBackStack()
                    .commit();
        } else{

            if (getSupportFragmentManager().getBackStackEntryCount()>4){
                getSupportFragmentManager().popBackStack(
                        getSupportFragmentManager().getBackStackEntryAt(0).getId(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE );
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(contaitnerId, fragment, fragmentTag)
                    .addToBackStack(backStackTag)
                    .commit();
        }
    }



}
