package com.example.montour.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.montour.callbacks.IMonCallback;
import com.example.montour.callbacks.IOnMonumentData;
import com.example.montour.models.MonumentItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MonumentManager {

    private static String LOG_TAG = "MonumentManager";

    private static MonumentManager instance = null;
    private ArrayList<MonumentItem> mMonumentList = new ArrayList<>();
    private String url = "https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek3/MapServer/207/query?where=Postcode%20%3D%20%272060%27%20OR%20Postcode%20%3D%20%272000%27%20OR%20Postcode%20%3D%20%272020%27%20OR%20Postcode%20%3D%20%272140%27%20OR%20Postcode%20%3D%20%272600%27&outFields=OBJECTID,Naam,Straatnaam,Huisnr,District,Postcode&outSR=4326&f=json";
    private static RequestQueue queue;
    private static MonumentsOpenHelper dbHelper;

    private IOnMonumentData listener;


    private MonumentManager(){

    }

    public static MonumentManager getInstance(Context ctx, IOnMonumentData listener) {
        if(instance == null) {
            instance = new MonumentManager();
            instance.queue = Volley.newRequestQueue(ctx);
            instance.dbHelper = new MonumentsOpenHelper(ctx);
            instance.listener = listener;

            // instance.setMonumentListLatLong(instance.queue);
        }

        return instance;
    }

    public SQLiteDatabase getWritableDatabase(){
        return instance.dbHelper.getWritableDatabase();
    }

    public ArrayList<MonumentItem> getmMonumentList(){
        return this.mMonumentList;
    }


    public void loadMonumentsFromDb(SQLiteDatabase db){
        try {
            instance.mMonumentList = this.dbHelper.getAllMonuments(db);
            this.listener.dataChanged();
        } catch (Exception e) {
            e.printStackTrace();
            this.loadMonumentsAsJson();
        }
    }
    public void loadMonuments(SQLiteDatabase db){
        try {
            this.mMonumentList = this.dbHelper.getAllMonuments(db);
            instance.listener.dataChanged();

        } catch (Exception e) {
            e.printStackTrace();
            this.loadMonumentsAsJson();

        }
    }

    public void loadMonumentsAsJson() {
        queue.add(instance.createJsonObjectRequest(new IMonCallback() {
            @Override
            public void onSuccess(Object obj) {
                JSONArray arr = (JSONArray)obj;
                try {
                    instance.addParsedMonumentToList(arr);
                    SQLiteDatabase db = instance.dbHelper.getWritableDatabase();
                    instance.dbHelper.storeMonumentListValuesIntoDb(instance.mMonumentList, db);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Object obj) {
                Log.e(LOG_TAG, "error: "+ obj);
            }
        }));

    }

    private JsonObjectRequest createJsonObjectRequest(final IMonCallback callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            callback.onSuccess(response.getJSONArray("features"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                });

        return jsonObjectRequest;

    }


    private void addParsedMonumentToList(JSONArray arr) throws JSONException {
        for (int i = 0; i < arr.length(); i++) {
            Log.v(LOG_TAG, arr.getJSONObject(i).toString());
            JSONObject obj = (arr.getJSONObject(i)).getJSONObject("attributes");
            MonumentItem item = new MonumentItem(obj);
            item.setLatLon(arr.getJSONObject(i).getJSONObject("geometry").getJSONArray("rings").getJSONArray(0).getJSONArray(0));
            instance.mMonumentList.add(item);
        }
        listener.dataChanged();

    }






}
