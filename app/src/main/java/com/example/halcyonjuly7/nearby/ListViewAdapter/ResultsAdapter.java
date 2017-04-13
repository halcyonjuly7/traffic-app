package com.example.halcyonjuly7.nearby.ListViewAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.halcyonjuly7.nearby.R;
import com.example.halcyonjuly7.nearby.ViewHolders.ResultsViewHolder;
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
    public ResultsAdapter(Context context, JSONArray data) {
        this.data = data;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences("server_info", Context.MODE_PRIVATE);
        this.url = prefs.getString("server_address", "");
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
        ResultsViewHolder view_holder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.results_layout, null);
            view_holder = new ResultsViewHolder();
            view_holder.name = (TextView) convertView.findViewById(R.id.place_title);
            view_holder.image = (ImageView)convertView.findViewById(R.id.place_image);
            convertView.setTag(view_holder);
        } else {
            view_holder = (ResultsViewHolder)convertView.getTag();
        }
        try {
            JSONObject current_item = (JSONObject)getItem(position);
            String place_name = current_item.getString("name");
            view_holder.name.setText(place_name);
            String photo_ref = current_item.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            Picasso.with(context).load(String.format("%s/photo?place_id=%s", this.url, photo_ref)).into(view_holder.image);

        } catch (JSONException error) {
            error.printStackTrace();

        }
        return convertView;
    }
}
