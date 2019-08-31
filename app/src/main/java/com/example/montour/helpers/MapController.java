package com.example.montour.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.example.montour.R;
import com.example.montour.activities.MapRouteActivity;
import com.example.montour.models.MonumentItem;
import com.google.android.gms.maps.GoogleMap;
import com.mapbox.mapboxsdk.Mapbox;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.ArrayList;

public class MapController {
    private Context context;
    public MapController(Context context){

    }

    public void afterOnMapReady(MapboxMap initiatedMap){
      initiatedMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                /*style.addImage("marker-icon-id",
                        BitmapFactory.decodeResource(
                               context.getResources(), R.drawable.mapbox_marker_icon_default));
*/
            }
        });
    }

    public void addMarkers(MapboxMap map, ArrayList<MonumentItem> items){
        items.forEach((MonumentItem item)-> {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(item.getLatLong().latitude, item.getLatLong().longitude))
                    .title(item.getMyName()));
        });
        map.addMarker(new MarkerOptions()
                .position(new LatLng(items.get(0).getLatLong().latitude, items.get(0).getLatLong().longitude))
                .title(items.get(0).getMyName()));

    }

    public void addAllPolylines(MapboxMap map, ArrayList<MonumentItem> items){
        PolyCreator creator = new PolyCreator();
        map.addPolyline(new PolylineOptions()
                .addAll(creator.getPolygonLatLngList(items))
                .add(new LatLng(items.get(0).getLatLong().latitude, items.get(0).getLatLong().longitude))
                .color(Color.parseColor("#3bb2d0"))
                .width(2));

    }




}
