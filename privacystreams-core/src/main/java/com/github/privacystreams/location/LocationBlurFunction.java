package com.github.privacystreams.location;

import com.github.privacystreams.utils.LocationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 29/12/2016.
 * A function that computes the blurred coordinates
 */
class LocationBlurFunction extends LocationProcessor<LatLng> {
    private final double blurMeters;

    LocationBlurFunction(String latLngField, double blurMeters) {
        super(latLngField);
        this.blurMeters = blurMeters;
        this.addParameters(blurMeters);
    }

    @Override
    protected LatLng processLocation(LatLng latLng) {
        return LocationUtils.blurLatLng(latLng, this.blurMeters);
    }
}
