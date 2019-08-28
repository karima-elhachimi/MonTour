package com.example.montour.helpers;

import android.content.Context;
import android.util.Log;

import com.example.montour.R;
import com.example.montour.callbacks.IMonCallback;
import com.example.montour.callbacks.IOnDistanceCalculated;
import com.example.montour.models.MonumentItem;
import com.google.maps.errors.ApiException;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.matrix.v1.MapboxMatrix;
import com.mapbox.api.matrix.v1.models.MatrixResponse;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DistanceCalculator {
    private IOnDistanceCalculated listener;
    private Context context;
    private double maxDistance = 0;
    private MonumentItem[] orderedMonuments;
    private ArrayList<MonumentItem> monumentItems;

    private MonumentItem furthest;


    public DistanceCalculator(IOnDistanceCalculated listener, Context context, ArrayList<MonumentItem> items){
        this.listener = listener;
        this.context = context;
        this.monumentItems = items;


    }

    public void orderMonumentsByDistance(){
        this.calculateDistanceFromFirstItem();
    }

    //The monument that is the most far away is placed in the middle so that the other monuments are on the way to and back.
    public void getFurthestMonument(MonumentItem item, Double distance){
        if(this.maxDistance < distance){
            this.maxDistance = distance;
            this.furthest = item;
        }
    }

    public void orderMonuments(){
        int half = this.monumentItems.size()/2;
        this.monumentItems.remove(this.furthest);
        this.monumentItems.add(half, furthest);
        this.listener.distanceCalculated(this.monumentItems);
    }

    public void calculateDistanceFromFirstItem(){
            for(int i = 1; i < this.monumentItems.size(); i++){
                try {

                    MonumentItem destination = this.monumentItems.get(i);
                    this.calculateDistance(this.monumentItems.get(0), destination, new IMonCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            Double distance = (Double)obj;
                           getFurthestMonument(destination, distance);
                        }

                        @Override
                        public void onError(Object obj) {

                        }

                    });


                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }




    }

    public void calculateDistance(MonumentItem start, MonumentItem destination, IMonCallback callback) throws InterruptedException, ApiException, IOException {
        Point pointStart = Point.fromLngLat(start.getLatLong().longitude, start.getLatLong().latitude);
        Point pointDest = Point.fromLngLat(destination.getLatLong().longitude, destination.getLatLong().latitude);
        ArrayList<Point> points = new ArrayList<>();
        points.add(pointStart);
        points.add(pointDest);


        MatrixResponse.Builder matrixResponse;

        MapboxMatrix distCalcer = MapboxMatrix.builder()
                .accessToken(this.context.getResources().getString(R.string.mapbox_api_key))
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .coordinates(points)
                .build();
         distCalcer.enqueueCall(new Callback<MatrixResponse>() {
            @Override
            public void onResponse(Call<MatrixResponse> call,
                                   Response<MatrixResponse> response) {
                Log.v("distCalcer", "Distance between "+start.getMyName()+" and "+destination.getMyName());
               Double duration = response.body().durations().get(0)[1];
                Log.v("distCalcer", duration.toString());
                //calculatedDurations = MatrixResponse.builder(response.);
                callback.onSuccess(duration);


            }

            @Override
            public void onFailure(Call<MatrixResponse> call, Throwable t) {
                Log.v("distCalcer","calculating distance/duration failed.");

            }




        });

    }

    public ArrayList<MonumentItem> getOrderedMonumentItems(){

        return monumentItems;
    }





}
