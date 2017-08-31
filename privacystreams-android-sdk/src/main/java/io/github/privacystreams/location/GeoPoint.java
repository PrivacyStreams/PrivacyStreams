package io.github.privacystreams.location;

/**
 * Created by xiaobing1117 on 2017/8/30.
 */

public class GeoPoint {

    public double x;
    public double y;
    public long timestamp;

    public double index;
    public double start_index;
    public double end_index;

    public GeoPoint(double xx, double yy){
        x = xx;
        y = yy;

    }

    public GeoPoint(double xx, double yy, long timestamp){
        x=xx;
        y=yy;
        this.timestamp = timestamp;
    }

    public GeoPoint(GeoPoint p){
        x = p.x;
        y = p.y;
        timestamp = p.timestamp;
    }

    public GeoPoint(String str){
        int index = str.indexOf(",");
        x = Double.parseDouble(str.substring(0,index-1));
        y = Double.parseDouble(str.substring(index+1));
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public long getTimestamp(){
        return timestamp;
    }

    public String toString() {
        return "("+x+","+y+","+timestamp+");";
    }
}