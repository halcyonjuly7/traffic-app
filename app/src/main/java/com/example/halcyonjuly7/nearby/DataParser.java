package com.example.halcyonjuly7.nearby;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by halcyonjuly7 on 3/19/17.
 */

public class DataParser implements Iterator<JSONObject>{
    private JSONArray json_data;
    private int index = 0;
    public DataParser(JSONArray json_data ) {
        this.json_data = json_data;
    }

    @Override
    public boolean has_next() {
        return !(index == json_data.length());
    }

    @Override
    public JSONObject next() {
        if(has_next()) {
            try {
                JSONObject data = json_data.getJSONObject(index++);
                return data;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
}
