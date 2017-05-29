package com.example.halcyonjuly7.nearby.Utils;

import com.example.halcyonjuly7.nearby.Containers.Tuple;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by halcyonjuly7 on 4/19/17.
 */

public class ZipCoordHelper {
    String zip_coords;
    public ZipCoordHelper(String zip_coords) {
        this.zip_coords = zip_coords;
    }

    public List<Tuple<String, LatLng>> get_latlng() {
        List<Tuple<String, LatLng>> data = new ArrayList<>();
        try {
            JSONArray json_data = new JSONArray(zip_coords);
            for(int index=0; index < json_data.length(); index++) {
                JSONObject coord = json_data.getJSONObject(index);
                data.add(make_coord_map(coord));
            }

        } catch (JSONException e) {

        }
        return data;
    }

    private Tuple<String, LatLng> make_coord_map(JSONObject coord) throws JSONException {
        Double lat = Double.parseDouble(coord.getString("lat"));
        Double longi = Double.parseDouble(coord.getString("long"));
        return new Tuple<>(coord.getString("zip_code"), new LatLng(lat, longi));
    }

    public static Double calculate_distance(LatLng coord1, LatLng coord2) {
        final int R = 6371; // Radious of the earth
        Double lat1 = Double.parseDouble(String.valueOf(coord1.latitude));
        Double lon1 = Double.parseDouble(String.valueOf(coord1.longitude));
        Double lat2 = Double.parseDouble(String.valueOf(coord2.latitude));
        Double lon2 = Double.parseDouble(String.valueOf(coord2.longitude));
        Double latDistance = toRad(lat2-lat1);
        Double lonDistance = toRad(lon2-lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = R * c;
        return distance;

    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }

}
