package com.example.montour.activities;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.montour.R;
import com.example.montour.callbacks.IOnMonumentData;
import com.example.montour.helpers.MonumentManager;
import com.example.montour.models.MonumentItem;
import com.example.montour.models.MonumentSelection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, IOnMonumentData{

    private MonumentSelection selection;
    private ArrayList<MarkerOptions> allMarkers = new ArrayList<>();
    private GoogleMap mMap;
    private MonumentManager manager;
    private LatLng center = new LatLng(new Double("51.219411"), new Double("4.416129"));
    private Location myLocation;
    private static int REQUEST_CODE_ASK_PERMISSIONS = 11211;
    private Intent outgoingIntent;
    private FloatingActionButton goToRoutesFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.manager = MonumentManager.getInstance(this, this);
        this.selection = MonumentSelection.getInstance();

        this.goToRoutesFab = (FloatingActionButton) findViewById(R.id.go_to_route);
        this.outgoingIntent = new Intent(this, MapRouteActivity.class);
        this.goToRoutesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(outgoingIntent);
            }
        });


    }

    private void createMarkers(ArrayList<MonumentItem> monumentList) {
        for(MonumentItem item : monumentList) {
            MarkerOptions markerOptions = this.createMarker(item);
            this.mMap.addMarker(markerOptions).setTag(item);
            this.allMarkers.add(markerOptions);
        }
    }

    private void updateMarkers(){
        this.allMarkers.clear();
        this.mMap.clear();
        this.createMarkers(this.manager.getmMonumentList());
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        this.checkLocationPermission();

       // mMap.setOnMarkerClickListener(this);
        this.markerIsClicked();


        this.addMonumentsToMap();


    }



    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestPermissions(new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);

            Toast.makeText(this, "Please share your location so the app can generate a route for you!", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 11211:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    // Permission Denied
                    Toast.makeText( this,"Need your location to generate your route" , Toast.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    public  void getLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        this.myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (myLocation == null)
        {
            this.myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        }
    }




    private void addToSelection(Marker marker) {
        MonumentItem item = (MonumentItem) marker.getTag();
        try {
            this.selection.addToMonumentList(item);
            Toast.makeText(this, "Monument added to selection", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            this.selection.removeFromMonumentList(item);
        }

        this.toggleMarkerSelection(item, marker);
    }

    private void addMarkersToMap(){

    }

    @Override
    public void dataChanged() {
        this.addMonumentsToMap();


    }

    private void toggleMarkerSelection(MonumentItem item, MarkerOptions marker){
        if(this.selection.isMonumentInList(item)) {
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else {
            marker.icon(BitmapDescriptorFactory.defaultMarker());
        }

    }
    private void toggleMarkerSelection(MonumentItem item, Marker marker){
        if(this.selection.isMonumentInList(item)) {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        }

    }



    public void addMonumentsToMap(){

       this.createMarkers(this.manager.getmMonumentList());

        this.displayCurrentLocation();
    }

    public MarkerOptions createMarker(MonumentItem item) {
        MarkerOptions marker = new MarkerOptions().position(item.getLatLong()).title(item.getMyName());
        this.toggleMarkerSelection(item, marker);
        return marker;
    }

    public  void displayCurrentLocation(){
        CameraPosition cameraPosition = new CameraPosition.Builder().
                target(this.center).
                tilt(60).
                zoom(12).
                bearing(0).
                build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }



    public void markerIsClicked(){
        this.mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.v("marker clicker" , marker.getTitle());
                addToSelection(marker);
                toggleMarkerSelection((MonumentItem)marker.getTag(), marker) ;
                return false;
            }
        });
    }
}
