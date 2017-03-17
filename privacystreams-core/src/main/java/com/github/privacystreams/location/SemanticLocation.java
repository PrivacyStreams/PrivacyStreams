package com.github.privacystreams.location;

/**
 * Created by fanglinchen on 1/11/17.
 */

public class SemanticLocation {

    private String semanticLocationName;
    private GeoPoint geoPoint;

    public SemanticLocation(String semanticLocationName, GeoPoint geoPoint){
        this.semanticLocationName = semanticLocationName;
        this.geoPoint = geoPoint;
    }

    public GeoPoint getGeoPoint(){
        return this.geoPoint;
    }

    public String getSemanticLocationName(){
        return this.semanticLocationName;
    }

}
