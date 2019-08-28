package com.example.montour.models;

import com.example.montour.helpers.MonumentsDatabaseContract.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MonumentItem {
    private String myName;
    private String myStreetName;
    private int myHousenumber;
    private String myPostalcode;
    private String myDistrict;
    private LatLng myLatLng;
    private final String LOG_TAG = "MonumentItem";
    private double distance;

    public MonumentItem(){
        this.myName = null;
        this.myStreetName = null;
        this.myHousenumber = 0;
        this.myPostalcode = null;
        this.myDistrict = null;
    }

    public MonumentItem(String name, String street, int houseNr, String postalcode, String district  ) {
        this.myName = name;
        this.myStreetName = street;
        this.myHousenumber = houseNr;
        this.myPostalcode = postalcode;
        this.myDistrict = district;
    }

    public MonumentItem(JSONObject obj ) throws JSONException {
        this.myName= obj.getString("Naam");
        this.myStreetName = obj.getString("Straatnaam");
        this.setMyHousenumber(obj.getString("Huisnr"));
        this.myPostalcode = obj.getString("Postcode");
        this.myDistrict = obj.getString("District");
    }

    public MonumentItem(Cursor cursor){
        this.setMyName(cursor.getString(1));
        this.setMyStreetName(cursor.getString(2));
        this.setMyHousenumber(cursor.getString(3));
        this.setMyPostalcode(cursor.getString(4));
        this.setMyDistrict(cursor.getString(5));
        this.setLatLon(cursor.getDouble(6), cursor.getDouble(7));
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyStreetName() {
        return myStreetName;
    }

    public void setMyStreetName(String myStreetName) {
        this.myStreetName = myStreetName;
    }

    public int getMyHousenumber() {
        return myHousenumber;
    }

    public void setMyHousenumber(String myHousenumber) {
        if(myHousenumber.toString() == "null")
            this.myHousenumber = 0;
        else
            this.myHousenumber = Integer.parseInt(myHousenumber);
    }

    public String getMyPostalcode() {
        return myPostalcode;
    }

    public void setMyPostalcode(String myPostalcode) {
        this.myPostalcode = myPostalcode;
    }

    public String getMyDistrict() {
        return myDistrict;
    }

    public void setMyDistrict(String myDistrict) {
        this.myDistrict = myDistrict;
    }

    public String getAdress(){
        return this.myStreetName +" "+this.myHousenumber+" "+" "+this.myDistrict;
    }

    public ContentValues createValues(){
        ContentValues values = new ContentValues();
        values.put(MonumentInfoEntry.COLUMN_NAME, this.getMyName());
        values.put(MonumentInfoEntry.COLUMN_STREETNAME, this.getMyStreetName());
        values.put(MonumentInfoEntry.COLUMN_HOUSENUMBER, this.getMyHousenumber());
        values.put(MonumentInfoEntry.COLUMN_POSTALCODE, this.getMyPostalcode());
        values.put(MonumentInfoEntry.COLUMN_DISTRICT, this.getMyDistrict());
        values.put(MonumentInfoEntry.COLUMN_LAT, this.myLatLng.latitude);
        values.put(MonumentInfoEntry.COLUMN_LON, this.myLatLng.longitude);

        return values;
    }

    public LatLng getLatLong(){
        /*
        return LatLongGenerator.requestLatLong(getAdress(), new IMonCallback() {
            @Override
            public void onSuccess(Object obj) {
                try {
                    double lat = ((JSONObject) obj).getDouble("lat");
                    double lon = ((JSONObject) obj).getDouble("lon");
                    myLatLng = new LatLng(lat, lon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Object obj) {
                String msg = ((VolleyError) obj).getMessage();
                Log.e(LOG_TAG, msg);

            }
        });*/

        return this.myLatLng;




    }

    public void setLatLon(JSONArray jsonArray) {
        Log.v(LOG_TAG, "jsonArray lat lon: "+jsonArray.toString());
        try {
            this.myLatLng = new LatLng(jsonArray.getDouble(1), jsonArray.getDouble(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void setLatLon(double lat, double lon) {
            this.myLatLng = new LatLng(lat, lon);
    }

    public void calculateDistance(){

    }

}
