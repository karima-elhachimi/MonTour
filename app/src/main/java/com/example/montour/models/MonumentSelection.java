package com.example.montour.models;

import com.example.montour.helpers.MonumentExistsInListException;

import java.util.ArrayList;

public class MonumentSelection {
    private static MonumentSelection instance = null;

    private static ArrayList<MonumentItem> monuments;
    private MonumentSelection(){

    }

    public static MonumentSelection getInstance(){
        if(instance == null) {
            instance = new MonumentSelection();
            instance.monuments = new ArrayList<>();
        }
        return instance;
    }


    public ArrayList<MonumentItem> getSelectedMonuments(){
        return this.monuments;
    }



    public void addToMonumentList(MonumentItem item) throws Exception{

        if(this.isMonumentInList(item)) {
            this.removeFromMonumentList(item);
            throw new MonumentExistsInListException("Monument is removed");
        }
        else if(this.monuments.size() > 10) {
            throw new Exception("You can't select more than 10 Monuments");
        }
        else
            this.monuments.add(item);
    }

    public void removeFromMonumentList(MonumentItem item){
        this.monuments.remove(item);
    }

    public boolean isMonumentInList(MonumentItem item) {
        return this.monuments.contains(item);
    }

    public void calculateRoute(){}

    public void createMarkersFromRoute(){

    }

    public void determineFarthestPoint(){

    }

    public void orderByDistance(){

    }

    public void addRouteToMap(){
        //
    }






}
