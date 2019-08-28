package com.example.montour.models;

import android.util.Log;

import com.example.montour.callbacks.IMonCallback;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LatLongGenerator {

    public static final String LOG_TAG = "LatLongGenerator";
    JsonObjectRequest req;
    public static String url_latlon = "  https://nominatim.openstreetmap.org/search?";


    public static JsonArrayRequest requestLatLong(String addres, IMonCallback callback) {
        //https://nominatim.openstreetmap.org/search?q=Antwerp&format=json&limit=1

        Double lat = null;
        Double lng = null;

        url_latlon += "q=" + addres + "format=json&limit=1";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,
                url_latlon,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONObject obj = response.getJSONObject(0);
                            callback.onSuccess(obj);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Log.e(LOG_TAG, error.getMessage());
                    }
                }
        );

        return req;

    }


}
