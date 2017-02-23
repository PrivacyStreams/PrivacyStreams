package com.github.privacystreams.location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 29/12/2016.
 * A function that computes the blurred coordinates
 */
class LocationBlurFunction extends LocationProcessor<List<Double>> {
    private final double blurMeters;

    LocationBlurFunction(String coordinatesField, double blurMeters) {
        super(coordinatesField);
        this.blurMeters = blurMeters;
        this.addParameters(blurMeters);
    }

    @Override
    protected List<Double> processLocation(double latitude, double longitude) {
        List<Double> blurred_coordinates = new ArrayList<>();
        // TODO blur the latitude and longitude and return the blurred coordinates
        double blurred_latitude = latitude;
        double blurred_longitude = longitude;
        blurred_coordinates.add(blurred_latitude);
        blurred_coordinates.add(blurred_longitude);
        return blurred_coordinates;
    }
}
