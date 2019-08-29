package com.example.montour.helpers;

import com.example.montour.models.MonumentItem;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PolyCreator {

    private List<com.mapbox.mapboxsdk.geometry.LatLng> polygonLatLngList = new ArrayList<>();

    public PolyCreator(){

    }


    private void addLatLngs(ArrayList<MonumentItem> items) {
        for (MonumentItem item : items) {
            this.polygonLatLngList.add(new com.mapbox.mapboxsdk.geometry.LatLng(item.getLatLong().latitude, item.getLatLong().longitude));
        }
        this.polygonLatLngList.add(new com.mapbox.mapboxsdk.geometry.LatLng(items.get(0).getLatLong().latitude, items.get(0).getLatLong().longitude));
    }

    public Iterable<com.mapbox.mapboxsdk.geometry.LatLng> getPolygonLatLngList(ArrayList<MonumentItem> items){
        this.addLatLngs(items);
        return ( Iterable<com.mapbox.mapboxsdk.geometry.LatLng>)this.polygonLatLngList;
    }


}

