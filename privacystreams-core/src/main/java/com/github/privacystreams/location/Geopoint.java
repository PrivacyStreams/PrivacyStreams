package com.github.privacystreams.location;

import android.location.Location;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by fanglinchen on 3/15/17.
 */

public class GeoPoint {
    private double x;
    private double y;
    public long timestamp ;

    GeoPoint(Location location){
        x = location.getLatitude();
        y = location.getLongitude();
    }
    GeoPoint(double xx, double yy){
        x = xx;
        y = yy;
    }


    GeoPoint(double xx, double yy, long timestamp){
        x=xx;
        y=yy;
        this.timestamp = timestamp;
    }

    public GeoPoint(double xx, double yy,int hour,int minute ){
        x=xx;
        y=yy;

    }
    public GeoPoint(GeoPoint p){
        x = p.x;
        y = p.y;
    }

    public GeoPoint(String str){
        int index = str.indexOf(",");
        x = Double.parseDouble(str.substring(0,index-1));
        y = Double.parseDouble(str.substring(index+1));
    }
    public long offsetTimeByMinute(long time,double offset){
        return time+=offset*1000*60;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public ArrayList<Double> toList(){
        return new ArrayList<Double>
                (Arrays.asList(new Double[]{getX(),
                        getY()}));
    }
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String toString() {
        return x+","+y;
    }
    public  double Distance(GeoPoint b){
        Double distance = (this.x-b.x)*(this.x-b.x) +(this.y-b.y)*(this.y-b.y);
        return distance;
    }
}
