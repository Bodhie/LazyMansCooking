package com.group10.lazymanscooking.Controllers;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.group10.lazymanscooking.R;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Shake detectors
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Menu listeners
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setStartFragment();
        setSearchbarKeyListener();
        setShakeDetector();
        createAd();
    }

    public void createAd() {
        //Google addview
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void setSearchbarKeyListener() {
        // Enter handler for search of recipe title
        final EditText edittext = (EditText) findViewById(R.id.txtSearch);
        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    RecipesFragment f = new RecipesFragment();
                    Bundle args = new Bundle();
                    args.putString("search", String.valueOf(edittext.getText()));
                    f.setArguments(args);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.pager, f);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }
                return false;
            }
        });
    }

    public void setShakeDetector() {
        // On shaking
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                TextView search = (TextView) findViewById(R.id.txtSearch);
                String input = search.getText().toString();
                if(!input.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Shaken ", Toast.LENGTH_LONG).show();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(500);
                    SearchFunction();
                }
            }
        });
    }

    public void setStartFragment()
    {
        //Set start page
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipesFragment myFragment = new RecipesFragment();
        fragmentTransaction.add(R.id.pager, myFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.logout)
                .setMessage(R.string.DialogMessage)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the selected item action bar
        int id = item.getItemId();
        Fragment newFragment = new RecipesFragment();
        
        if (id == R.id.action_search) {
            TextView search = (TextView) findViewById(R.id.txtSearch);
            String ssearch = search.getText().toString();
            System.out.println(ssearch);
            RecipesFragment f = new RecipesFragment();
            Bundle args = new Bundle();
            args.putString("search", ssearch);
            f.setArguments(args);
            newFragment = f;
            System.out.println("search");
        }
        else if(id == R.id.action_advancedSearch)
        {
            newFragment = new SearchIngredientCategoryFragment();
        }
        // Create new transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.pager, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        return super.onOptionsItemSelected(item);
    }

    public void SearchFunction(){
        // Retrieve the inserted titel
        TextView search = (TextView) findViewById(R.id.txtSearch);
        String ssearch = search.getText().toString();

        RecipesFragment f = new RecipesFragment();
        // Add params to new Fragment
        Bundle args = new Bundle();
        args.putString("search", ssearch);
        f.setArguments(args);
        System.out.println("search");

        // Create new transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.pager, f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view items.
        int id = item.getItemId();
        Fragment newFragment = new RecipesFragment();

        if (id == R.id.nav_overview) {
            newFragment = new RecipesFragment();
        } else if (id == R.id.nav_add_recipe) {
            newFragment = new addRecipeFragment();
        } else if (id == R.id.nav_favorite_recipe) {
            RecipesFragment f = new RecipesFragment();
            // Add params to new Fragment
            Bundle args = new Bundle();
            args.putBoolean("favorite" ,true);
            f.setArguments(args);
            newFragment = f;
        }  else if (id == R.id.nav_my_recipe) {
            RecipesFragment f = new RecipesFragment();
            // Add params to new Fragment
            Bundle args = new Bundle();
            args.putBoolean("myRecipe" ,true);
            f.setArguments(args);
            newFragment = f;
        } else if (id == R.id.nav_options) {
            newFragment = new OptionFragment();
        } else if (id == R.id.nav_logout) {
            ParseUser.logOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        // Create new transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.pager, newFragment).addToBackStack("Back");
        transaction.commit();

        // Close slide menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
