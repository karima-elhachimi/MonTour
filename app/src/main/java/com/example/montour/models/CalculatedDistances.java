package com.example.montour.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CalculatedDistances {
    private Map<MonumentItem, Double> monumentItemsAndDistance = new HashMap<MonumentItem, Double>();
    private ArrayList<MonumentItem> orderedMonuments = new ArrayList<>();

    public CalculatedDistances(){

    }

    public void addDestinationWithDistance(MonumentItem destination, Double distance){
        this.monumentItemsAndDistance.put(destination, distance);
    }

    private  MonumentItem getFurthestDestination(){
        Double maxDistance = 0.0;
        MonumentItem furthest = new MonumentItem();

        for (Map.Entry<MonumentItem, Double> entry : this.monumentItemsAndDistance.entrySet()) {
            MonumentItem item = entry.getKey();
            Double distance = entry.getValue();
            if (distance > maxDistance) {
                maxDistance = distance;
                furthest = item;
            }

        }


        return furthest;
    }

    private void matchMonumentItemsToDistance(ArrayList<MonumentItem> items, Double[] distances){
        for(int i = 0; i <items.size() ; i++){
            this.addDestinationWithDistance(items.get(i), distances[i]);
        }
    }



    private void orderMonumentItemsWithFurthestInTheMiddle(){
        MonumentItem furthest = this.getFurthestDestination();

        for (Map.Entry<MonumentItem, Double> entry : this.monumentItemsAndDistance.entrySet()) {
            if(entry.getKey() != furthest || entry.getValue() != 0.0) {
                MonumentItem item = entry.getKey();
                this.orderedMonuments.add(item);
            }

        }

        this.orderedMonuments.add(this.orderedMonuments.size()/2, furthest);

    }

    public ArrayList<MonumentItem> getOrderedMonuments(){
        return this.orderedMonuments;
    }

    public void createOrderedMonumentList(ArrayList<MonumentItem> items, Double[] distances){
        this.matchMonumentItemsToDistance(items, distances);
        this.orderMonumentItemsWithFurthestInTheMiddle();
    }


}
