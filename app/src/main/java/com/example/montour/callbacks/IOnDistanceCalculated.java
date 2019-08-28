package com.example.montour.callbacks;

import com.example.montour.models.MonumentItem;

import java.util.ArrayList;

public interface IOnDistanceCalculated {
    void distanceCalculated(ArrayList<MonumentItem> items);
}
