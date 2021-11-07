package com.example.lab_6_m2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {

    // Somewhere in Australia
    private final LatLng mDestinationLatLng = new LatLng(43.07591, -89.40433);
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;//
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_map);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            // code to display marker
            googleMap.addMarker(new MarkerOptions()
                    .position(mDestinationLatLng)
                    .title("Destination"));
            Log.d(TAG, "Here is before display Location");
            displayMyLocation();
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayMyLocation();
            }
        }
    }

    private void displayMyLocation(){
        //Check if permission granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        Log.d(TAG, "Here is permission " + permission);
        Log.d(TAG, "Here is the denied code in the package: " + PackageManager.PERMISSION_DENIED);
        // If not, ask for it
        if (permission == PackageManager.PERMISSION_DENIED){
            Log.d(TAG, "Here is permission denied");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }

        // If permission granted, display marker at current location
        else{
            Log.d(TAG, "Here I'm granted permission, but still haven't create the marker");
            mFusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(this, task ->{
                        Location mLastKnownLocation = task.getResult();
                        //Log.d(TAG, "Check my current position: " + mLastKnownLocation.getLatitude() + "and" + mLastKnownLocation.getLongitude());
                        if(task.isSuccessful() && mLastKnownLocation != null){

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(-33,151))
                                    .title("My Last Location"));
                            mMap.addPolyline(new PolylineOptions().add(
                                    new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()),
                                    mDestinationLatLng
                            ));
                        }
                    });
        }
    }
}