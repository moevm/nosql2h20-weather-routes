package nosql2h20.weather.routes.model.requests;

import nosql2h20.weather.routes.model.Point;

import java.util.List;

public class AddObjectRequest {

    private String name;
    private String street;
    private String houseNumber;
    private List<Point> points;

    public AddObjectRequest() {
    }

    public AddObjectRequest(String name, String street, String houseNumber, List<Point> points) {
        this.name = name;
        this.street = street;
        this.houseNumber = houseNumber;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
