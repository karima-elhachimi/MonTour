package com.example.montour.helpers;

import android.graphics.Color;

import com.example.montour.models.MonumentItem;
import com.google.android.gms.maps.GoogleMap;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;

public class MarkerHandler {
    //todo: alles wat met markers te maken heeft heir genereren

    public MarkerHandler(){

    }

    public void createMapboxMarkers(MapboxMap mapbox, ArrayList<MonumentItem> items){

            items.forEach((MonumentItem item)-> {
                mapbox.addMarker(new MarkerOptions()
                        .position(new LatLng(item.getLatLong().latitude, item.getLatLong().longitude))
                        .title(item.getMyName()));
            });


    }

    public void createPolylinesForMapbox(MapboxMap mapbox, ArrayList<MonumentItem> items){
        PolyCreator creator = new PolyCreator();
        mapbox.addPolyline(new PolylineOptions()
                .addAll(creator.getPolygonLatLngList(items))
                .color(Color.parseColor("#3bb2d0"))
                .width(2));
    }

    public void createGoogleMarkers(GoogleMap map, ArrayList<MonumentItem> items){
        for(MonumentItem item : items) {
            com.google.android.gms.maps.model.MarkerOptions markerOptions = new com.google.android.gms.maps.model.MarkerOptions().position(item.getLatLong()).title(item.getMyName());
            map.addMarker(markerOptions).setTag(item);
        }

    }



}
