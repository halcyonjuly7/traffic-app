package com.example.halcyonjuly7.nearby.AsyncTasks;

import android.os.AsyncTask;

import com.example.halcyonjuly7.nearby.Utils.RequestHelper;
import com.example.halcyonjuly7.nearby.ViewHolders.ResultsViewHolder;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by halcyonjuly7 on 3/26/17.
 */

public class AsyncGetPlaceMetaData extends AsyncTask<Void, Void, JSONObject> {

    private LatLng dist_1;
    private LatLng dist_2;
    private ResultsViewHolder view_holder;

    public AsyncGetPlaceMetaData(LatLng dist_1, LatLng dist_2, ResultsViewHolder view_holder) {
        this.dist_1 = dist_1;
        this.dist_2 = dist_2;
        this.view_holder = view_holder;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        Map<String, String> params = new HashMap<>();
        params.put("lat_1", Double.toString(dist_1.latitude));
        params.put("lon_1", Double.toString(dist_1.longitude));
        params.put("lat_2", Double.toString(dist_2.latitude));
        params.put("lon_2", Double.toString(dist_2.longitude));
        String response = RequestHelper.get("http://45.55.198.11:7777/place_data", params);
        try {
            JSONObject data = new JSONObject(response);
            return data;
        } catch (JSONException e) {
            return null;
        }

    }

    @Override
    protected void onPostExecute(JSONObject data) {
        try {
            JSONArray rows = data.getJSONArray("rows");
            JSONObject first_item = rows.getJSONObject(0).getJSONArray("elements").getJSONObject(0);
            String distance = first_item.getJSONObject("distance").getString("text");
            String duration = first_item.getJSONObject("duration").getString("text");
            view_holder.duration = duration;
            view_holder.distance = distance;
            view_holder.place_distance.setText(distance);
            view_holder.place_duration.setText(duration);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
