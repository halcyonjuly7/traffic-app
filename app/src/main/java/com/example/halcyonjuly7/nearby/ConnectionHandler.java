package com.example.halcyonjuly7.nearby;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.halcyonjuly7.nearby.Activities.ResultsActivity;
import com.example.halcyonjuly7.nearby.Interfaces.IProcessQuery;
import com.example.halcyonjuly7.nearby.ListViewAdapter.ResultsAdapter;
import com.example.halcyonjuly7.nearby.Modals.PlaceDetails;
import com.example.halcyonjuly7.nearby.Utils.RequestHelper;
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

public class ConnectionHandler extends AsyncTask<Void, Void, JSONObject> {
    Map<String, String> req_args;
    Context context;
    public ConnectionHandler(Context context, Map<String, String> args) {
        this.req_args = args;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {

        String response = RequestHelper.get("http://45.55.198.11:7777/nearest", req_args);
        JSONObject data = null;
        try {
            data = new JSONObject(response);
//            data = j_data.getJSONObject("data").getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onPostExecute(JSONObject data) {
        ((IProcessQuery)context).process_query(data);
    }
}
