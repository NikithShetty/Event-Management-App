package com.nikith_shetty.vgroup;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    homeFragment homeFragment;
    eventFragment eventFragment;
    collegesFragment collegesFragment;
    accountsFragment accountsFragment;
    placesFragment placesFragment;
    FragmentTransaction transaction;
    int placeHolderId = R.id.content_area;
    NavigationView navigationView;
    Fragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "This is a SnackBar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        //Add fragment to the View
        homeFragment = new homeFragment();
        homeFragment.setArguments(navigationView);
//        transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(placeHolderId, homeFragment);
//        transaction.commit();
//        homeFragment.setArguments(navigationView);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(placeHolderId, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();

//        //Sign In Button implementation
//        Button signin = (Button)findViewById(R.id.button_signIn);
//        signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getParent(),LoginActivity.class));
//            }
//        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                frag = getSupportFragmentManager().findFragmentByTag("visible_fragment");
                if(frag instanceof homeFragment){
                    getSupportActionBar().setTitle("V Group");
                }
                if(frag instanceof eventFragment){
                    getSupportActionBar().setTitle("Events");
                }
                if(frag instanceof accountsFragment){
                    getSupportActionBar().setTitle("Accounts");
                }
                if(frag instanceof collegesFragment){
                    getSupportActionBar().setTitle("Colleges");
                }
                if(frag instanceof placesFragment){
                    getSupportActionBar().setTitle("Places");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if(homeFragment==null)homeFragment = new homeFragment();
            homeFragment.setArguments(navigationView);
            makeTransaction(placeHolderId,homeFragment);
            getSupportActionBar().setTitle("V Group");
        }else if (id == R.id.nav_events) {
            if(eventFragment==null)eventFragment = new eventFragment();
            eventFragment.setArguments(navigationView);
            makeTransaction(placeHolderId,eventFragment);
            getSupportActionBar().setTitle("Events");
        } else if (id == R.id.nav_account) {
            if(accountsFragment==null)accountsFragment = new accountsFragment();
            accountsFragment.setArguments(navigationView);
            makeTransaction(placeHolderId,accountsFragment);
            getSupportActionBar().setTitle("Accounts");
        } else if (id == R.id.nav_colleges) {
            if(collegesFragment==null)collegesFragment = new collegesFragment();
            collegesFragment.setArguments(navigationView);
            makeTransaction(placeHolderId,collegesFragment);
            getSupportActionBar().setTitle("Colleges");
        } else if (id == R.id.nav_places) {
            if(placesFragment==null)placesFragment = new placesFragment();
            placesFragment.setArguments(navigationView);
            makeTransaction(placeHolderId,placesFragment);
            getSupportActionBar().setTitle("Places");
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void makeTransaction(int replaceId, Fragment frag){
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(replaceId, frag,"visible_fragment");
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
