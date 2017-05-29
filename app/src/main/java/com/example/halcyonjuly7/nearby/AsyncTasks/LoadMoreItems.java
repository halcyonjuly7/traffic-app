package com.example.halcyonjuly7.nearby.AsyncTasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.example.halcyonjuly7.nearby.Activities.ResultsActivity;
import com.example.halcyonjuly7.nearby.Interfaces.ILoadMoreItems;
import com.example.halcyonjuly7.nearby.Utils.RequestHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by halcyonjuly7 on 5/6/17.
 */

public class LoadMoreItems extends AsyncTask<Void, Void, String> {
    private StringBuilder next_page_token;
    private Context context;

    public LoadMoreItems(StringBuilder next_page_token, Context context) {
        this.next_page_token = next_page_token;
        this.context = context;

    }

    @Override
    protected void onPostExecute(String response) {
        try {
            JSONObject json_response = new JSONObject(response);
            if(json_response.getString("status").equals("OK")) {
                next_page_token.delete(0, next_page_token.length());
                String next_token = json_response.has("next_page_token") ? json_response.getString("next_page_token"): null;
                ((ILoadMoreItems)context).update_adapter(next_token, json_response.getJSONArray("results"));
            }






        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String doInBackground(Void... voids) {
        Map<String, String> args = new HashMap<>();
        args.put("page_token", next_page_token.toString());
        String response = RequestHelper.get("http://45.55.198.11:7777/nearest/next_page", args);
        return response;

    }
}
