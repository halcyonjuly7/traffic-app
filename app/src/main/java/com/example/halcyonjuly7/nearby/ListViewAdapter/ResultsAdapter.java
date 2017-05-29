package com.example.halcyonjuly7.nearby.ListViewAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.halcyonjuly7.nearby.AsyncTasks.AsyncGetPlaceMetaData;
import com.example.halcyonjuly7.nearby.R;
import com.example.halcyonjuly7.nearby.ViewHolders.ResultsViewHolder;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by halcyonjuly7 on 4/2/17.
 */

public class ResultsAdapter extends BaseAdapter {

    JSONArray data;
    Context context;
    String url;
    LatLng current_coordinates;
    public ResultsAdapter(Context context, JSONArray data, LatLng current_coordinates) {
        this.data = data;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences("server_info", Context.MODE_PRIVATE);
        this.url = prefs.getString("server_address", "");
        this.current_coordinates = current_coordinates;
    }

    @Override
    public int getCount() {
        return data.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return data.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject current_item = null;
        String place_name = null;
        JSONObject location_object;
        LatLng place_coordinates = null;
        String photo_ref = null;

        try {
            current_item = (JSONObject)getItem(position);
            place_name = current_item.getString("name");
            location_object = current_item.getJSONObject("geometry").getJSONObject("location");
            place_coordinates = new LatLng(location_object.getDouble("lat"), location_object.getDouble("lng"));
            photo_ref = current_item.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
        } catch (JSONException e) {

        }


        ResultsViewHolder view_holder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.results_layout, null);
            view_holder = new ResultsViewHolder();
            view_holder.name = (TextView) convertView.findViewById(R.id.place_title);
            view_holder.image = (ImageView)convertView.findViewById(R.id.place_image);
            view_holder.place_distance = (TextView)convertView.findViewById(R.id.place_distance);
            view_holder.place_duration = (TextView)convertView.findViewById(R.id.place_duration);


            convertView.setTag(view_holder);
        } else {
            view_holder = (ResultsViewHolder)convertView.getTag();
        }

        view_holder.name.setText(place_name);
        if(photo_ref != null) {
            Picasso.with(context).cancelRequest(view_holder.image);
            Picasso.with(context).load(String.format("%s/photo?place_id=%s", this.url, photo_ref)).resize(80, 80).into(view_holder.image);
        }


        if(view_holder.duration == null) {
            AsyncGetPlaceMetaData place_metadata = new AsyncGetPlaceMetaData(current_coordinates, place_coordinates, view_holder);
            place_metadata.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            view_holder.place_duration.setText(view_holder.duration);
            view_holder.place_distance.setText(view_holder.distance);
        }
        return convertView;
    }





//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ResultsViewHolder view_holder;
//        if(convertView == null) {
//            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.results_layout, null);
//            view_holder = new ResultsViewHolder();
//            view_holder.name = (TextView) convertView.findViewById(R.id.place_title);
//            view_holder.image = (ImageView)convertView.findViewById(R.id.place_image);
//            view_holder.place_distance = (TextView)convertView.findViewById(R.id.place_distance);
//            view_holder.place_duration = (TextView)convertView.findViewById(R.id.place_duration);
//            convertView.setTag(view_holder);
//        } else {
//            view_holder = (ResultsViewHolder)convertView.getTag();
//        }
//        try {
//            JSONObject current_item = (JSONObject)getItem(position);
//            String place_name = current_item.getString("name");
//            JSONObject location_object = current_item.getJSONObject("geometry").getJSONObject("location");
//            LatLng place_coordinates = new LatLng(location_object.getDouble("lat"), location_object.getDouble("lng"));
//
//            view_holder.name.setText(place_name);
//            String photo_ref = current_item.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
//
//            Picasso.with(context).load(String.format("%s/photo?place_id=%s", this.url, photo_ref)).resize(80, 80).into(view_holder.image);
//            if(view_holder.duration == null) {
//                AsyncGetPlaceMetaData place_metadata = new AsyncGetPlaceMetaData(current_coordinates, place_coordinates, view_holder);
//                place_metadata.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            } else {
//                view_holder.place_duration.setText(view_holder.duration);
//                view_holder.place_distance.setText(view_holder.distance);
//            }
//
//        } catch (JSONException error) {
//            error.printStackTrace();
//
//        }
//        return convertView;
//    }
}
