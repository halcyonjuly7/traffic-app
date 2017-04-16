package com.example.halcyonjuly7.nearby;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.halcyonjuly7.nearby.Activities.ResultsActivity;
import com.example.halcyonjuly7.nearby.ListViewAdapter.ResultsAdapter;
import com.example.halcyonjuly7.nearby.Modals.PlaceDetails;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by halcyonjuly7 on 3/19/17.
 */

public class ConnectionHandler extends AsyncTask<Void, Void, JSONArray> {
    Map<String, String> req_args;
    Context context;
    GoogleApiClient google_client;
    public ConnectionHandler(Context context, GoogleApiClient google_client, Map<String, String> args) {
        this.req_args = args;
        this.context = context;
        this.google_client = google_client;
    }

    public String get(String url, Map<String, String> args) {
        try {
            QueryBuilder query_builder = new QueryBuilder(url);
            URL built_url = new URL(query_builder.build_query(args));
            HttpURLConnection conn = (HttpURLConnection) built_url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader data = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder response_body = new StringBuilder();
                while ((line = data.readLine()) != null) {
                    response_body.append(line);

                }
                return response_body.toString();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected JSONArray doInBackground(Void... params) {
        String response = get("http://45.55.198.11:7777/nearest?", req_args);
        JSONArray data = null;
        try {
            JSONObject j_data = new JSONObject(response);
            data = j_data.getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(JSONArray data) {
        ListView location_list = (ListView) ((Activity) context).findViewById(R.id.location_list);
        final ResultsAdapter res_adapter = new ResultsAdapter(context, data);
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
                            String name = places.get(0).getName().toString();
                            LatLng coords = places.get(0).getLatLng();
                            PlaceDetails place_modal = new PlaceDetails();
                            Bundle args = new Bundle();
                            args.putParcelable("coords", coords);
                            args.putString("place_name", name);
                            place_modal.setArguments(args);
                            place_modal.show(((ResultsActivity) context).getSupportFragmentManager(),"hello");

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
