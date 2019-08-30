package com.example.montour.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.montour.R;
import com.example.montour.callbacks.IOnDistanceCalculated;
import com.example.montour.callbacks.IOnMonumentData;
import com.example.montour.callbacks.IOnToggleClicked;
import com.example.montour.helpers.DistanceCalculator;
import com.example.montour.helpers.PolyCreator;
import com.example.montour.models.MonumentItem;
import com.example.montour.models.MonumentListAdapter;
import com.example.montour.models.MonumentSelection;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;

public class MapRouteActivity extends AppCompatActivity implements IOnDistanceCalculated, IOnToggleClicked, OnMapReadyCallback {

    private MonumentSelection selection = MonumentSelection.getInstance();
    private MapView map;
    private MapboxMap mapbox;
    private RecyclerView monumentsRv;
    private MonumentListAdapter monumentListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private DistanceCalculator distanceCalculator;
    private ArrayList<MonumentItem> sortedItems;

    private FloatingActionButton redoFab;
    private Intent outgoingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1Ijoia2FyaW1hZWwiLCJhIjoiY2p6c2pwc3RkMHpwNjNuczFsMTR4ZWRhZCJ9.jgfZbN2VPth7Hi_eJYq3-A");
        setContentView(R.layout.activity_map_route);
        /*
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_routes);
        mapFragment.getMapAsync(this);*/
        this.map = (MapView)findViewById(R.id.map_routes);
        this.redoFab = (FloatingActionButton) findViewById(R.id.redo_fab);

        this.map.onCreate(savedInstanceState);
        this.distanceCalculator = new DistanceCalculator(this,this, this.selection.getSelectedMonuments() );
        this.distanceCalculator.orderMonumentsByDistance();

        this.map.getMapAsync(this);
        this.setOnClickListeners();

;
    }


    /*
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        for(MonumentItem item: selection.getSelectedMonuments()){
            MarkerOptions marker = new MarkerOptions().position(item.getLatLong()).title(item.getMyName());
            this.map.addMarker(marker).setTag(item);
        }
        //this.addAllPolylines(this.selection.getSelectedMonuments());


    }*/ //google maps changed to mapbox

    public void addAllPolylines(ArrayList<MonumentItem> items){
        PolyCreator creator = new PolyCreator();
        this.mapbox.addPolyline(new PolylineOptions()
                .addAll(creator.getPolygonLatLngList(items))
                .color(Color.parseColor("#3bb2d0"))
                .width(2));

    }

    public void setOnClickListeners(){
        this.redoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapRouteActivity.this, "Removing selected items and returning to list.", Toast.LENGTH_LONG).show();
                selection.clearSelection();
                outgoingIntent = new Intent(MapRouteActivity.this, ListActivity.class);
                startActivity(outgoingIntent);

            }
        });


    }


    @Override
    public void handleClickedToggle(Object o) {


    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapbox = mapboxMap;
        this.mapbox.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                style.addImage("marker-icon-id",
                        BitmapFactory.decodeResource(
                                MapRouteActivity.this.getResources(), R.drawable.mapbox_marker_icon_default));

                GeoJsonSource geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(
                        Point.fromLngLat(-87.679, 41.885)));
                style.addSource(geoJsonSource);

                SymbolLayer symbolLayer = new SymbolLayer("layer-id", "source-id");
                symbolLayer.withProperties(
                        PropertyFactory.iconImage("marker-icon-id")
                );
                style.addLayer(symbolLayer);

            }
        });

    }


    @Override
    public void distanceCalculated(ArrayList<MonumentItem> items) {
        this.sortedItems = items;
        this.addAllPolylines(items);
        this.monumentsRv = (RecyclerView) findViewById(R.id.route_result_rv);
        this.layoutManager = new LinearLayoutManager(this);
        this.monumentsRv.setLayoutManager(this.layoutManager);
        this.monumentListAdapter = new MonumentListAdapter(this, items);
        this.monumentsRv.setAdapter(this.monumentListAdapter);
        this.monumentListAdapter.notifyDataSetChanged();

    }
}
