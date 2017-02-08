package com.adaptivemedia.adaptivenarrativemobile;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Joel on 24/01/2017.
 */

public class locationProvider extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    double longitude, latitude;
    TextView mLatitudeText, mLongitudeText;

    private GoogleApiClient mClient;
    private LocationCallback mLocationCallback;
    private Context mContext;
    private LocationRequest mLocationRequest;
    public static final String TAG = locationProvider.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    public locationProvider(Context context, LocationCallback callback) {
        mLongitudeText = (TextView) findViewById(R.id.mLongitudeValue);
        mLatitudeText = (TextView) findViewById(R.id.mLatitudeValue);

        //Create an instance of GoogleAPIClient to make connection with
        mClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationCallback = callback;

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(30 * 1000); // 30 second, in milliseconds

        mContext = context;
    }


    public interface LocationCallback {
        void handleNewLocation(Location location);
    }

    public void connect() {
        mClient.connect();
    }

    public void disconnect() {
        if (mClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mClient, this);
            mClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");

        Location location = LocationServices.FusedLocationApi.getLastLocation(mClient);

        if (location != null){
            mLocationCallback.handleNewLocation(location);
            mLatitudeText.setText(String.valueOf(location.getLatitude()));
            mLongitudeText.setText(String.valueOf(location.getLongitude()));
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, mLocationRequest, this);
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }

        //checkLocationPermission();

    }

    @Override
    public void onConnectionSuspended(int i) {
        //If connection to Google Play services was lost for some reason. Call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult Result) {
         /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */

        if (Result.hasResolution() && mContext instanceof Activity) {
            try {
                Activity activity = (Activity)mContext;
                // Start an Activity that tries to resolve the error
                Result.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
        /*
         * Thrown if Google Play services canceled the original
         * PendingIntent
         */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
        /*
         * If no resolution is available, display a dialog to the
         * user with the error.
         */
            // Refer to the javadoc for ConnectionResult to see what error codes might be returned in onConnectionFailed.
            Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + Result.getErrorCode());
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocationCallback.handleNewLocation(location);
    }

//    public boolean checkLocationPermission() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//                //Prompt the user once explanation has been shown
//                ActivityCompat.requestPermissions(this,
//                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(this,
//                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    Location location = LocationServices.FusedLocationApi.getLastLocation(mClient);
//
//                    if (location != null){
//                        mLocationCallback.handleNewLocation(location);
//                        mLatitudeText.setText(String.valueOf(location.getLatitude()));
//                        mLongitudeText.setText(String.valueOf(location.getLongitude()));
//                    } else {
//                        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, mLocationRequest, this);
//                        Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
//                    }
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
}
