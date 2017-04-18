package com.example.halcyonjuly7.nearby.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.halcyonjuly7.nearby.ConnectionHandler;
import com.example.halcyonjuly7.nearby.Interfaces.IProcessQuery;
import com.example.halcyonjuly7.nearby.ListViewAdapter.ResultsAdapter;
import com.example.halcyonjuly7.nearby.Modals.PlaceDetails;
import com.example.halcyonjuly7.nearby.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, IProcessQuery {
    GoogleApiClient google_client;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);
        Intent intent = getIntent();
        ArrayList<String> zip_codes = intent.getStringArrayListExtra("zip_codes");
        String arg = stringify_list(zip_codes);
        Map<String, String> args = new HashMap<>();
        context = this;
        google_client = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();
        args.put("zip_codes",arg);
        ConnectionHandler handler = new ConnectionHandler(context, args);
        handler.execute();
    }

    private String stringify_list(ArrayList<String> zip_codes) {
        StringBuilder builder = new StringBuilder();
        java.util.Iterator<String> iterable = zip_codes.iterator();
        String last  =iterable.next();
        while(iterable.hasNext()) {
            String item = iterable.next();
            builder.append(item + ",");
        }
        builder.append(last);
        return builder.toString();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void process_query(JSONArray data) {
        if(data != null) {
            ListView location_list = (ListView)findViewById(R.id.location_list);
            final ResultsAdapter res_adapter = new ResultsAdapter(this, data);
            location_list.setAdapter(res_adapter);
            location_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    JSONObject clicked_item = (JSONObject) res_adapter.getItem(position);
                    try {
                        Places.GeoDataApi.getPlaceById(google_client, clicked_item.getString("place_id")).setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(@NonNull PlaceBuffer places) {
                                String status = places.getStatus().toString();
                                String name = places.get(0).getName() != null ? places.get(0).getName().toString() : "";
                                String address = places.get(0).getAddress() != null ? places.get(0).getAddress().toString() : "";
                                String number = places.get(0).getPhoneNumber() != null ? places.get(0).getPhoneNumber().toString() : "";
                                String website_url = places.get(0).getWebsiteUri() != null ? places.get(0).getWebsiteUri().toString() : "";
                                LatLng coords = places.get(0).getLatLng();
                                PlaceDetails place_modal = new PlaceDetails();
                                Bundle args = new Bundle();
                                args.putParcelable("coords", coords);
                                args.putString("place_name", name);
                                args.putString("place_number", number);
                                args.putString("place_address", address);
                                args.putString("place_website", website_url);
                                place_modal.setArguments(args);
                                place_modal.show(getSupportFragmentManager(), "hello");
                                places.release();
                            }
                        });
                    } catch (JSONException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }
            });

        }

    }
}
