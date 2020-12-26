package nosql2h20.weather.routes.model;

import java.util.List;

public class Route {

    private final List<CoordinatePoint> coordinatePoints;
    private final int precipitationAvg;
    private final double totalDistance;

    public Route(List<CoordinatePoint> coordinatePoints, double totalDistance, int precipitationAvg) {
        this.coordinatePoints = coordinatePoints;
        this.totalDistance = totalDistance;
        this.precipitationAvg = precipitationAvg;
    }

    public List<CoordinatePoint> getCoordinatePoints() {
        return coordinatePoints;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public int getPrecipitationAvg() {
        return precipitationAvg;
    }
}
