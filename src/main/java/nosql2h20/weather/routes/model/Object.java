package nosql2h20.weather.routes.model;

import java.util.List;

public class Object {

    private final String name;
    private final String street;
    private final String houseNumber;
    private final List<Point> points;

    public Object(String name, String street, String houseNumber, List<Point> points) {
        this.name = name;
        this.street = street;
        this.houseNumber = houseNumber;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public List<Point> getPoints() {
        return points;
    }
}
