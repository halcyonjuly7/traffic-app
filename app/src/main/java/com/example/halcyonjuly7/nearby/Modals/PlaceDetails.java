package com.example.halcyonjuly7.nearby.Modals;

import com.example.halcyonjuly7.nearby.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by halcyonjuly7 on 4/5/17.
 */

public class PlaceDetails extends DialogFragment implements OnMapReadyCallback{
    private MapView map_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.places_info, null);
        map_view = (MapView)view.findViewById(R.id.map);
        map_view.onCreate(savedInstanceState);
        map_view.getMapAsync(this);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        map_view.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        map_view.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map_view.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map_view.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final LatLng coords = getArguments().getParcelable("coords");
        String place_name = getArguments().getString("place_name");
        googleMap.addMarker(new MarkerOptions().position(coords).title(place_name));
        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 12.0f));

    }
}
