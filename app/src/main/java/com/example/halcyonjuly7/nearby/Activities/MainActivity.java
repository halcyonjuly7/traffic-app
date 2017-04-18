package com.example.halcyonjuly7.nearby.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.halcyonjuly7.nearby.Interfaces.IZipCodeDelete;
import com.example.halcyonjuly7.nearby.Modals.DeleteZipCodeModal;
import com.example.halcyonjuly7.nearby.Utils.NetworkHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


import com.example.halcyonjuly7.nearby.R;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, IZipCodeDelete {
    private ArrayAdapter<String> zip_adapter;
    private GridView grid_view;
    private MainActivity context;
    List<String> zip_codes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        set_up_shared_preferences();
        grid_view = (GridView)findViewById(R.id.zip_codes);
        context = this;
        zip_codes = new ArrayList<>();
        zip_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, zip_codes);
        grid_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putInt("zip_index", i);
                DeleteZipCodeModal delete_modal = new DeleteZipCodeModal();
                delete_modal.setArguments(args);
                delete_modal.show(getSupportFragmentManager(), "zip_delete");
                return true;
            }
        });
        grid_view.setAdapter(zip_adapter);
        final EditText zip_text = (EditText)findViewById(R.id.zip_text);
        Button add_zip = (Button)findViewById(R.id.add_zip);
        FloatingActionButton search_zip = (FloatingActionButton)findViewById(R.id.search_zips);
        search_zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkHelper.is_connected(context)) {
                    if(zip_codes.size() != 0) {
                        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                        intent.putStringArrayListExtra("zip_codes", (ArrayList<String>)zip_codes);
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "There are no zip codes to search", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Not connected to network", Toast.LENGTH_SHORT).show();
                }
            }
        });
        add_zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable new_code = zip_text.getText();
                if(new_code.length()  != 0) {
                    zip_codes.add(new_code.toString());
                    zip_adapter.notifyDataSetChanged();
                    new_code.clear();
                }

            }
        });
    }

    private void set_up_shared_preferences() {
        SharedPreferences shared_pref = getSharedPreferences("server_info", Context.MODE_PRIVATE);
        if(!shared_pref.contains("server_address")) {
            SharedPreferences.Editor editor = shared_pref.edit();
            editor.putString("server_address", "http://45.55.198.11:7777");
            editor.commit();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void delete_zip(int index) {
//        grid_view.re(index);
        zip_codes.remove(index);
        zip_adapter.notifyDataSetChanged();

    }

}
