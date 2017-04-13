package com.example.halcyonjuly7.nearby.Modals;

import com.example.halcyonjuly7.nearby.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by halcyonjuly7 on 4/5/17.
 */

public class PlaceDetails extends android.app.DialogFragment{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity  = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final LatLng coords = getArguments().getParcelable("coords");
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.places_info, null);
        MapView map_view = (MapView)view.findViewById(R.id.map);
        map_view.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions().position(coords));
            }
        });
        builder.setView(view);
        return builder.create();

    }
}
