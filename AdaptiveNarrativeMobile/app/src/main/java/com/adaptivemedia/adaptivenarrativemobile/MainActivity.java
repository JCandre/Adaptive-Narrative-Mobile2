package com.adaptivemedia.adaptivenarrativemobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.v4.app.FragmentManager;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public final static int PAGES = 5;
    // You can choose a bigger number for LOOPS, but you know, nobody will fling
    // more than 1000 times just in order to test your "infinite" ViewPager :D
    public final static int LOOPS = 1000;
    public final static int FIRST_PAGE = PAGES * LOOPS / 2;

    public final static String AUDIO_FILE_NAME = "track.mp3";

    public PagerAdapter adapter;
    public ViewPager pager;
    private FragmentManager fragmentManager;
    private boolean viewIsAtHome;
    SwitchPreference locationSwitch;
    SharedPreferences sharedPref;


    private OnSharedPreferenceChangeListener listener =
            new OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    if (key.equals("location_switch")) {
                       //myClass.BooleanVariable = prefs.getBoolean("location_switch", true);
                        boolean value = prefs.getBoolean("location_switch", false);
                        if (value == true){
                            locationPrefTrue();
                        } else {
                            locationPrefFalse();
                        }
                    }
                }
            };

    //location provider
    Button getLocalTogg;
    protected FusedLocationProviderApi fusedLocationProviderApi;
    protected Location mLastLocation;
    protected GoogleApiClient mClient;
    LocationRequest mLocationRequest;
    public static final String TAG = "MainActivity";
    double mlongitude, mlatitude;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getLocalTogg = (Button) findViewById(R.id.getLocal);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        sharedPref = this.getSharedPreferences("pref_general", MODE_PRIVATE);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(listener);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ability to add podcasts to library coming soon!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayView(R.id.nav_library_layout);

        pager = (ViewPager) findViewById(R.id.myviewpager);
        adapter = new PagerAdapter(this, this.getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setPageTransformer(false, adapter);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        pager.setCurrentItem(FIRST_PAGE);

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.setOffscreenPageLimit(3);

        // Set margin for pages as a negative number, so a part of next and
        // previous pages will be showed
        pager.setPageMargin(-500);

        getLocation();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }


    public void locationPrefTrue(){
        Toast.makeText(this, "True", Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Polling", true);
        editor.commit();
        //mClient.connect();
    }

    public void locationPrefFalse(){
        Toast.makeText(this, "False", Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Polling", false);
        editor.commit();
        //mClient.disconnect();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the Library fragment
            displayView(R.id.nav_library_layout); //display the Library fragment
        } else {
            moveTaskToBack(true);  //If view is in Library fragment, exit application
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

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

    public void displayView(int viewId) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_library_layout:
                fragment = new LibraryFragment();
                title  = "Library";
                break;
            case R.id.nav_ldata_layout:
                fragment = new locationFragment();
                title = "Location";
                break;
            case R.id.nav_help_layout:
                fragment = new helpFragment();
                title = "Help";
                break;

            case R.id.nav_settings_layout:
                Intent settingsIntent = new Intent(this,SettingsActivity.class);
                startActivity(settingsIntent);
                break;

            case R.id.nav_share:
                //fragment = new helpFragment();
                title = "Share";
                break;

            case R.id.nav_send:
                //fragment = new helpFragment();
                title = "Share";
                break;

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            //ft.addToBackStack(null);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displayView(item.getItemId());
        return true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }

    private void getLocation(){
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(30 * 1000); // 30 second, in milliseconds
        fusedLocationProviderApi = LocationServices.FusedLocationApi;

        //Create an instance of GoogleAPIClient to make connection with
        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (mClient != null) {

        }
    }

    public void toggleGPSUpdates(View view) {
        if(!checkLocation())
            return;
        Button button = (Button) view;

        boolean polling = sharedPref.getBoolean("Polling", false);
        if (polling == true){
            if (button.getText().equals(getResources().getString(R.string.pause))) { //If button string is set to pause
                button.setText(R.string.resume); //Set button string to resume
                if (mClient.isConnected()) {
                    mClient.disconnect();
                }

            } else {

                mClient.connect();
                button.setText(R.string.pause);
            }
        } else
        {
            Toast.makeText(this, "Enable Location Services in app settings", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onConnected(Bundle arg0) {
        Log.i(TAG, "Location services connected.");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
        mlongitude = mLastLocation.getLongitude();
        mlatitude = mLastLocation.getLatitude();

        if (mLastLocation != null){
            viewUpdate(String.valueOf(mlongitude), String.valueOf(mlatitude));
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnectionSuspended(int cause) {
        //If connection to Google Play services was lost for some reason. Call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());

    }

    @Override
    public void onLocationChanged(Location location) {
        mlongitude = location.getLongitude();
        mlatitude = location.getLatitude();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewUpdate(String.valueOf(mlongitude), String.valueOf(mlatitude));
            }
        });

    }


    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Location disabled'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS); //Where in settings to auto send the user to
                        startActivity(myIntent); //send user to settings
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }


    //send strings over to locationFragment class
    public void viewUpdate(String longTxt, String latTxt) {
        locationFragment frag = (locationFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (frag != null) {
            frag.update(longTxt, latTxt);
        }
    }

}
