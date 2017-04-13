package com.example.halcyonjuly7.nearby;

/**
 * Created by halcyonjuly7 on 3/19/17.
 */
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class QueryBuilderTest {

    @Test
    public void build_query_test() {
        String url = "http://hello.com/json?";
        QueryBuilder query_builder = new QueryBuilder(url);
        Map<String, String> arg_1 = new HashMap<String, String>();
        arg_1.put("name", "yolo,yolo");
        arg_1.put("age", "2");
        String query = query_builder.build_query(arg_1);
        assertEquals(query,"http://hello.com/json?name=yolo,yolo&age=2");


    }
}
