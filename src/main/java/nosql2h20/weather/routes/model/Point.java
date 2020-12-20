package nosql2h20.weather.routes.model;

public class Point {

    private final double latitude;
    private final double longitude;
    private final int precipitationValue;

    public Point(double latitude, double longitude, int precipitationValue) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.precipitationValue = precipitationValue;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getPrecipitationValue() {
        return precipitationValue;
    }
}
