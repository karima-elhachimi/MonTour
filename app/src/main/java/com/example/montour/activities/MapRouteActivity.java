package com.example.montour.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.montour.R;
import com.example.montour.callbacks.IOnDistanceCalculated;
import com.example.montour.callbacks.IOnMonumentData;
import com.example.montour.callbacks.IOnToggleClicked;
import com.example.montour.helpers.DistanceCalculator;
import com.example.montour.helpers.MapController;
import com.example.montour.helpers.PolyCreator;
import com.example.montour.models.MonumentItem;
import com.example.montour.models.MonumentListAdapter;
import com.example.montour.models.MonumentSelection;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
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
    private MapController mapCtrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1Ijoia2FyaW1hZWwiLCJhIjoiY2p6c2pwc3RkMHpwNjNuczFsMTR4ZWRhZCJ9.jgfZbN2VPth7Hi_eJYq3-A");
        setContentView(R.layout.activity_map_route);
        this.redoFab = (FloatingActionButton) findViewById(R.id.redo_fab);
        this.map = (MapView)findViewById(R.id.map_routes);
        this.map.onCreate(savedInstanceState);
        this.map.getMapAsync(this);
        this.mapCtrl = new MapController(this);

        this.distanceCalculator = new DistanceCalculator(this,this, this.selection.getSelectedMonuments() );
        this.distanceCalculator.orderMonumentsByDistance();


        this.setOnClickListeners();

;
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
        MonumentItem item = (MonumentItem) o;
        Log.v("clicked item", o.toString());

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapbox = mapboxMap;
        this.mapCtrl.afterOnMapReady(this.mapbox);
    }


    @Override
    public void distanceCalculated(ArrayList<MonumentItem> items) {
        this.mapCtrl.addAllPolylines(this.mapbox, items);
        this.mapCtrl.addMarkers(this.mapbox, items);

        this.monumentsRv = (RecyclerView) findViewById(R.id.route_result_rv);
        this.layoutManager = new LinearLayoutManager(this);
        this.monumentsRv.setLayoutManager(this.layoutManager);
        this.monumentListAdapter = new MonumentListAdapter(this, items);
        this.monumentsRv.setAdapter(this.monumentListAdapter);
        this.monumentListAdapter.notifyDataSetChanged();

    }
}
