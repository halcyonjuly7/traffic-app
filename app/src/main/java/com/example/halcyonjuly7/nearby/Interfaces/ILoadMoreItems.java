package com.example.halcyonjuly7.nearby.Interfaces;

import org.json.JSONArray;

/**
 * Created by halcyonjuly7 on 5/7/17.
 */

public interface ILoadMoreItems {
    void update_adapter(String next_page_token, JSONArray results);
}
