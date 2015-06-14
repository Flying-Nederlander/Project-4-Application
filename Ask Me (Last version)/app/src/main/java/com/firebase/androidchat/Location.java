package com.firebase.androidchat;

import android.os.Bundle;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;

/**
 * Created by FRANK LAPTOP on 12-6-2015.
 */
public class Location {
    public void onCreate(Bundle savedInstanceState) {
        GeoFire geoFire = new GeoFire(new Firebase(MainActivity.getSERVER_URL() + "location"));

        geoFire.getLocation("firebase-hq", new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                } else {
                    System.out.println(String.format("There is no location for key %s in GeoFire", key));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.err.println("There was an error getting the GeoFire location: " + firebaseError);
            }
        });




    }
}

