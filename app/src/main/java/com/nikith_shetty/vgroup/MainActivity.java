package com.nikith_shetty.vgroup;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.StormpathError;
import com.stormpath.sdk.models.UserProfile;

import javax.microedition.khronos.opengles.GL;

import helper.classes.Global;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, appTitle{

    FragmentTransaction transaction;
    int placeHolderId = R.id.content_area;
    NavigationView navigationView;
    View headerView;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //send nav View to global data class
        Global.setNavigationView(navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        //Add fragment to the View
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(placeHolderId, homeFragment.newInstance());
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpHeaderView();
    }

    private void setUpHeaderView() {
        if(Stormpath.accessToken() == null){
            Log.e("Login In Error", "No records");
                if(headerView != null)
                    navigationView.removeHeaderView(headerView);
                headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
                Button signin = (Button)headerView.findViewById(R.id.button_signIn);
                signin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (drawer.isDrawerOpen(GravityCompat.START))
                            drawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                });
        }else{
            String uName = Global.getUserName();
            if(uName.equals("")){
                getUserProfile();
                uName = Global.getUserName();
            }
            Log.e("Login In Success", "Records found - " + uName);
                if(headerView!=null)
                    navigationView.removeHeaderView(headerView);
                headerView = navigationView.inflateHeaderView(R.layout.nav_header_signedin);
                TextView userName = (TextView)headerView.findViewById(R.id.header_userName);
                userName.setText(uName);
        }
    }

    private void getUserProfile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (Global.getUserName().equals("")){
                    Stormpath.getUserProfile(new StormpathCallback<UserProfile>() {
                        @Override
                        public void onSuccess(UserProfile userProfile) {
                            Global.setUserName(userProfile.getFullName());
                        }

                        @Override
                        public void onFailure(StormpathError error) {
                            if(Global.getUserName() != "")
                                Global.setUserName("");
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUpHeaderView();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
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
        if (id == R.id.action_logout) {
            Global.removeUserName();
            Stormpath.logout();
            setUpHeaderView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation navigationView item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            makeTransaction(placeHolderId, homeFragment.newInstance());
//            getSupportActionBar().appTitle("V Group");
        }else if (id == R.id.nav_events) {
            makeTransaction(placeHolderId, eventFragment.newInstance());
//            getSupportActionBar().appTitle("Events");
        } else if (id == R.id.nav_account) {
            makeTransaction(placeHolderId, accountsFragment.newInstance());
//            getSupportActionBar().appTitle("Accounts");
        } else if (id == R.id.nav_colleges) {
            makeTransaction(placeHolderId, collegesFragment.newInstance());
//            getSupportActionBar().appTitle("Colleges");
        } else if (id == R.id.nav_places) {
            makeTransaction(placeHolderId, placesFragment.newInstance());
//            getSupportActionBar().appTitle("Places");
        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void makeTransaction(int replaceId, Fragment frag){
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(replaceId, frag,"visible_fragment");
        transaction.commit();

    }

    @Override
    public void onSetTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
