package nosql2h20.weather.routes.model;

public class Point {

    private double latitude;
    private double longitude;
    private int precipitationValue;

    public Point() {
    }

    public Point(double latitude, double longitude, int precipitationValue) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.precipitationValue = precipitationValue;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPrecipitationValue() {
        return precipitationValue;
    }

    public void setPrecipitationValue(int precipitationValue) {
        this.precipitationValue = precipitationValue;
    }
}
