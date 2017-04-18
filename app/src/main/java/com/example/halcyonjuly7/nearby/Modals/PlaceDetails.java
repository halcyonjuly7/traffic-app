package com.example.halcyonjuly7.nearby.Modals;

import com.example.halcyonjuly7.nearby.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by halcyonjuly7 on 4/5/17.
 */

public class PlaceDetails extends DialogFragment implements OnMapReadyCallback{
    private MapView map_view;
    Activity context;
    private final int REQUEST_CALL_PHONE = 1;
    private Bundle arguments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.places_info, null);
        context = getActivity();
        map_view = (MapView)view.findViewById(R.id.map);
        arguments = getArguments();
        TextView place_name = (TextView)view.findViewById(R.id.place_name);
        final TextView place_number = (TextView)view.findViewById(R.id.place_number);
        TextView place_url = (TextView)view.findViewById(R.id.place_website);
        place_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String web_url = arguments.getString("place_website");
                Intent web_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(web_url));
                startActivity(web_intent);
            }
        });
        place_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(context, android.Manifest.permission.CALL_PHONE)) {

                    } else {
                        ActivityCompat.requestPermissions(context,new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                    }
                } else {
                    start_call();
                }
            }
        });

        place_name.setText(arguments.getString("place_name"));
        place_number.setText("Call");
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

    private void start_call() {
        Intent call_intent = new Intent(Intent.ACTION_CALL);
        call_intent.setData(Uri.parse("tel:" + arguments.getString("place_number")));
        startActivity(call_intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PHONE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    start_call();
                }
                break;
        }
    }
}
