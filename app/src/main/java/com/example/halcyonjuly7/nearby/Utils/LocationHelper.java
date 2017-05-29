package com.example.halcyonjuly7.nearby.Utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by halcyonjuly7 on 5/1/17.
 */

public class LocationHelper implements LocationListener {
    Context context;
    LocationManager location_manager;

    public LocationHelper(Context context) {
        this.context = context;
    }

    public LatLng get_current_location() {
        location_manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean is_gps_enabled = location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean is_network_enabled = location_manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (is_gps_enabled || is_network_enabled) {
            try {
                location_manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 60 * 1, 10, this);
                Location location = null;
                if(is_gps_enabled) {
                    location = location_manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if(is_network_enabled) {
                    location = location_manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                if(location != null) {
                    return new LatLng(location.getLatitude(), location.getLongitude());
                }

                if(location_manager != null) {

                }
            }
            catch (SecurityException e) {

            }
        }
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
