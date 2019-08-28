package com.example.montour.models;

import java.util.List;

public interface IDirectionFinderListener {

    void onDirectionFinderStart();

    void onDirectionFinderSuccess(List<Route> routes);
}
