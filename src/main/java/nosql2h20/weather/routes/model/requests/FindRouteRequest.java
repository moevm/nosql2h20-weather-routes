package nosql2h20.weather.routes.model.requests;

import nosql2h20.weather.routes.model.CoordinatePoint;

public class FindRouteRequest {

    private CoordinatePoint from;
    private CoordinatePoint to;

    public FindRouteRequest() {
    }

    public FindRouteRequest(CoordinatePoint from, CoordinatePoint to) {
        this.from = from;
        this.to = to;
    }

    public CoordinatePoint getFrom() {
        return from;
    }

    public void setFrom(CoordinatePoint from) {
        this.from = from;
    }

    public CoordinatePoint getTo() {
        return to;
    }

    public void setTo(CoordinatePoint to) {
        this.to = to;
    }
}
