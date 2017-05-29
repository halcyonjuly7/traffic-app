package com.example.halcyonjuly7.nearby.Modals;

import com.example.halcyonjuly7.nearby.Containers.Tuple;
import com.example.halcyonjuly7.nearby.R;
import com.example.halcyonjuly7.nearby.Utils.ZipCoordHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


/**
 * Created by halcyonjuly7 on 4/5/17.
 */

public class PlaceDetails extends DialogFragment implements OnMapReadyCallback{
    private MapView map_view;
    Activity context;
    private final int REQUEST_CALL_PHONE = 2;
    private Bundle arguments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.places_info, null);
        context = getActivity();
        map_view = (MapView)view.findViewById(R.id.map);
        arguments = getArguments();
        TextView place_name = (TextView)view.findViewById(R.id.place_name);
        final ImageView place_number = (ImageView)view.findViewById(R.id.place_number);
        ImageView place_url = (ImageView)view.findViewById(R.id.place_website);
        ImageView place_share = (ImageView)view.findViewById(R.id.place_share);
        place_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share_intent = new Intent(Intent.ACTION_SEND);
                share_intent.putExtra(Intent.EXTRA_SUBJECT, getArguments().getString("place_address"));
                share_intent.setType("text/*");
                startActivity(Intent.createChooser(share_intent, "Select Message"));


            }
        });
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
        ZipCoordHelper zip_helper = new ZipCoordHelper(arguments.getString("zip_coords"));
        List<Tuple<String, LatLng>> zip_coords = zip_helper.get_latlng();
        if(zip_coords.size() > 0) {
            for(Tuple<String, LatLng> coord: zip_coords) {
                googleMap.addMarker(new MarkerOptions().position(coord.second)
                        .title(coord.first)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
        }

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 10.0f));

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
