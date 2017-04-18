package com.example.halcyonjuly7.nearby.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by halcyonjuly7 on 4/17/17.
 */

public class NetworkHelper {
    public static boolean is_connected(Context context) {
        ConnectivityManager con_manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network_status = con_manager.getActiveNetworkInfo();
        return network_status != null && network_status.isConnectedOrConnecting();
    }
}
