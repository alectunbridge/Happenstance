package alec.android.happenstance;

public class LatLong {
    public double lat;
    public double lng;

    public LatLong(double latitude, double longitude) {
        lat = latitude;
        lng = longitude;
    }

    public String toString(){
        return lat + ", " + lng;
    }
}
