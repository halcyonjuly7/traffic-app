package com.example.halcyonjuly7.nearby;

import java.util.*;

/**
 * Created by halcyonjuly7 on 3/19/17.
 */

public class QueryBuilder {
    String url;
    public QueryBuilder(String base_url) {
        url = base_url;
    }

    public String build_query(Map<String, String>query_args) {
        StringBuilder url_builder = new StringBuilder();
        url_builder.append(url);
        java.util.Iterator map_iterator = query_args.entrySet().iterator();
        Map.Entry first_entry = (Map.Entry) map_iterator.next();
        url_builder.append(String.format("?%s=%s", first_entry.getKey(), first_entry.getValue()));
        while(map_iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) map_iterator.next();
            url_builder.append(String.format("&%s=%s", entry.getKey(), entry.getValue()));
        }

        return url_builder.toString();

    }
}
