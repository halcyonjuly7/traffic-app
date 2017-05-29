package com.example.halcyonjuly7.nearby.Utils;

import com.example.halcyonjuly7.nearby.QueryBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by halcyonjuly7 on 5/1/17.
 */

public class RequestHelper {
    public static String get(String url, Map<String, String> args) {
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
}
