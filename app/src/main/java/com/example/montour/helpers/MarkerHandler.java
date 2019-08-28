package com.example.montour.helpers;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

public class MarkerHandler {
    //todo: alles wat met markers te maken heeft heir genereren

    public MarkerHandler(){

    }

    public void createMapboxMarkers(){
        GeoJsonSource geoJsonSource =
                new GeoJsonSource("source-id", Feature.fromGeometry(Point.fromLngLat(-87.679, 41.885)));

    }

    public void createGoogleMarkers(){}
}
