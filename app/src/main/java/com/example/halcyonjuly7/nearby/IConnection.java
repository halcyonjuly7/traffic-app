package com.example.halcyonjuly7.nearby;

import org.json.JSONObject;

/**
 * Created by halcyonjuly7 on 3/19/17.
 */

public interface IConnection<T> {
    JSONObject get(String url);
}
