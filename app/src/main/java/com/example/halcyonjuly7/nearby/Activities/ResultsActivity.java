package com.example.halcyonjuly7.nearby.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.halcyonjuly7.nearby.AsyncTasks.LoadMoreItems;
import com.example.halcyonjuly7.nearby.ConnectionHandler;
import com.example.halcyonjuly7.nearby.Interfaces.ILoadMoreItems;
import com.example.halcyonjuly7.nearby.Interfaces.IProcessQuery;
import com.example.halcyonjuly7.nearby.ListViewAdapter.ResultsAdapter;
import com.example.halcyonjuly7.nearby.Modals.PlaceDetails;
import com.example.halcyonjuly7.nearby.R;
import com.example.halcyonjuly7.nearby.Utils.LocationHelper;
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

public class ResultsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, IProcessQuery, ILoadMoreItems {
    GoogleApiClient google_client;
    Context context;
    Map<String, String> args;
    String stringed_list;
    Intent intent;
    String place_number;
    StringBuilder next_page_token= new StringBuilder();
    JSONArray results;
    ResultsAdapter res_adapter;
    ListView location_list;
    Button load_more;
    boolean show_modal = true;


    private void execute_connection() {
        google_client = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();
        args.put("zip_codes", stringed_list);
        args.put("type", intent.getStringExtra("place_type"));
        ConnectionHandler handler = new ConnectionHandler(context, args);
        handler.execute();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    execute_connection();
                } else {
                    Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent call_intent = new Intent(Intent.ACTION_CALL);
                    call_intent.setData(Uri.parse("tel:" + place_number));
                    startActivity(call_intent);
                }
                break;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);
        intent = getIntent();
        ArrayList<String> zip_codes = intent.getStringArrayListExtra("zip_codes");
        stringed_list = stringify_list(zip_codes);
        args = new HashMap<>();
        context = this;
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            execute_connection();
        }
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
    public void process_query(JSONObject data) {
        JSONArray zip_coords = null;
        String page_token = null;

        try {
            JSONObject response_body = data.getJSONObject("data");
            results = response_body.getJSONArray("results");
            zip_coords = data.getJSONArray("zip_coords");
            if (response_body.has("next_page_token")) {
                page_token = response_body.getString("next_page_token");
                next_page_token.append(page_token);

            }

        } catch (JSONException e) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            return;

        }

        if(results != null && zip_coords != null) {


            final String j_array = zip_coords.toString();
            LocationHelper loc_helper = new LocationHelper(this);
            res_adapter = new ResultsAdapter(this, results, loc_helper.get_current_location());
            location_list = (ListView)findViewById(R.id.location_list);
            if(!next_page_token.toString().isEmpty()) {
                load_more = new Button(context);
                load_more.setText("Load More");
                load_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(next_page_token.toString().isEmpty()) {
                            location_list.removeFooterView(load_more);
                            return;
                        }
                        LoadMoreItems async_load = new LoadMoreItems(next_page_token, context);
                        async_load.execute();

                    }
                });
                location_list.addFooterView(load_more);
            }

            location_list.setAdapter(res_adapter);
            location_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(show_modal) {
                        show_modal = false;
                        JSONObject clicked_item = (JSONObject) res_adapter.getItem(position);
                        try {
                            Places.GeoDataApi.getPlaceById(google_client, clicked_item.getString("place_id")).setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(@NonNull PlaceBuffer places) {
                                    String status = places.getStatus().toString();
                                    String name = places.get(0).getName() != null ? places.get(0).getName().toString() : "";
                                    String address = places.get(0).getAddress() != null ? places.get(0).getAddress().toString() : "";
                                    place_number = places.get(0).getPhoneNumber() != null ? places.get(0).getPhoneNumber().toString() : "";
                                    String website_url = places.get(0).getWebsiteUri() != null ? places.get(0).getWebsiteUri().toString() : "";
                                    LatLng coords = places.get(0).getLatLng();
                                    PlaceDetails place_modal = new PlaceDetails();
                                    Bundle args = new Bundle();
                                    args.putString("zip_coords", j_array);
                                    args.putParcelable("coords", coords);
                                    args.putString("place_name", name);
                                    args.putString("place_number", place_number);
                                    args.putString("place_address", address);
                                    args.putString("place_website", website_url);
                                    place_modal.setArguments(args);
                                    place_modal.show(getSupportFragmentManager(), "hello");
                                    places.release();
                                    show_modal=true;
                                }
                            });
                        } catch (JSONException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }


                }
            });

        } else {
            Toast.makeText(this, "thats not a valid zip code", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }


    @Override
    public void update_adapter(String next_page_token, JSONArray results) {
        if(next_page_token != null) {
            this.next_page_token.append(next_page_token);
        } else {
            location_list.removeFooterView(load_more);
        }

        for(int index = 0; index < results.length(); index++) {
            try {
                this.results.put(results.getJSONObject(index));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        res_adapter.notifyDataSetChanged();

    }
}
