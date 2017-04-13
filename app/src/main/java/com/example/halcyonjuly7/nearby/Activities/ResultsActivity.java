package com.example.halcyonjuly7.nearby.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.halcyonjuly7.nearby.ConnectionHandler;
import com.example.halcyonjuly7.nearby.Iterator;
import com.example.halcyonjuly7.nearby.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);
        Intent intent = getIntent();
        ArrayList<String> zip_codes = intent.getStringArrayListExtra("zip_codes");
        String arg = stringify_list(zip_codes);
        Map<String, String> args = new HashMap<>();
        GoogleApiClient google_client = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();
        args.put("zip_codes",arg);
        ConnectionHandler handler = new ConnectionHandler(this, google_client, args);
        handler.execute();

//        String response = handler.get("http://45.55.198.11:7777/nearest?",args);

//        Map<String, String> args = new HashMap<>();
//        ConnectionHandler con = new ConnectionHandler("45.55.198.11:7777/nearest?", )
    }


    private String stringify_list(ArrayList<String> zip_codes) {
        StringBuilder builder = new StringBuilder();
        java.util.Iterator<String> iterable = zip_codes.iterator();
        String last  =iterable.next();
        while(iterable.hasNext()) {
            String item = iterable.next();
            builder.append(item + ",");
        }
        builder.append(last);
        return builder.toString();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }
}
