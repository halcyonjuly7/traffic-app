package com.example.halcyonjuly7.nearby;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.halcyonjuly7.nearby.Activities.ResultsActivity;
import com.example.halcyonjuly7.nearby.Interfaces.IProcessQuery;
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
    public ConnectionHandler(Context context, Map<String, String> args) {
        this.req_args = args;
        this.context = context;
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
        ((IProcessQuery)context).process_query(data);
    }
}
