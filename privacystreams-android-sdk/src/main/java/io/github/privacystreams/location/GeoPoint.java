package io.github.privacystreams.location;


class GeoPoint {

    private double lat;
    private double lon;
    private long timestamp;


    GeoPoint(double lat, double lon, long timestamp) {
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
    }

    GeoPoint(GeoPoint p) {
        lat = p.getLat();
        lon = p.getLon();
        timestamp = p.timestamp;
    }

    void setLat(double lat) {
        this.lat = lat;
    }

    double getLat() {
        return lat;
    }

    void setLon(double lon) {
        this.lon = lon;
    }


    double getLon() {
        return lon;
    }

    void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String toString() {
        return "(" + lat + "," + lon + "," + timestamp + ");";
    }
}