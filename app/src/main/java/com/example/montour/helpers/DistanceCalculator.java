package com.example.montour.helpers;

import android.content.Context;
import android.util.Log;

import com.example.montour.R;
import com.example.montour.callbacks.IMonCallback;
import com.example.montour.callbacks.IOnDistanceCalculated;
import com.example.montour.models.CalculatedDistances;
import com.example.montour.models.MonumentItem;
import com.google.maps.errors.ApiException;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.matrix.v1.MapboxMatrix;
import com.mapbox.api.matrix.v1.models.MatrixResponse;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DistanceCalculator {
    private IOnDistanceCalculated listener;
    private Context context;
    private double maxDistance = 0;
    private MonumentItem[] orderedMonuments;
    private ArrayList<MonumentItem> monumentItems;
    private CalculatedDistances calculatedDistances;

    private MonumentItem furthest;


    public DistanceCalculator(IOnDistanceCalculated listener, Context context, ArrayList<MonumentItem> items){
        this.listener = listener;
        this.context = context;
        this.monumentItems = items;
        this.calculatedDistances = new CalculatedDistances();

    }

    public void orderMonumentsByDistance(){
        this.calculateDistanceFromFirstItem();
    }




    public void calculateDistanceFromFirstItem(){
        try {

            this.calculateDistance(new IMonCallback() {
                @Override
                public void onSuccess(Object obj) {
                    //Double distance = (Double)obj;
                    //calculatedDistances.addDestinationWithDistance(destination, distance);
                    listener.distanceCalculated(calculatedDistances.getOrderedMonuments());
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

    public void calculateDistance( IMonCallback callback) throws InterruptedException, ApiException, IOException {
        ArrayList<Point> points = new ArrayList<>();
        this.monumentItems.forEach((MonumentItem item ) -> {
            Point point = Point.fromLngLat(item.getLatLong().longitude, item.getLatLong().latitude);
            points.add(point);
            Log.v("calculateDistance", "added point => "+point.toString());
        });

        //todo: ipv van per duration een aparte callback, alle durations in een keer doorsturen en ordenen, of toevoegen aan een list en deze zo dorgeveni

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

                Log.v("distCalcer", "all distances: "+ response.body().durations().get(0));
                //calculatedDurations = MatrixResponse.builder(response.);
                Double[] distancesResponse = response.body().durations().get(0);

                calculatedDistances.createOrderedMonumentList(monumentItems, distancesResponse);
                callback.onSuccess(calculatedDistances.getOrderedMonuments());
                //calculatedDistances.addDestinationWithDistance(destination, duration);

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
