package nosql2h20.weather.routes.services;

import nosql2h20.weather.routes.model.Object;
import nosql2h20.weather.routes.model.Point;
import org.neo4j.driver.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static nosql2h20.weather.routes.services.Queries.*;
import static org.neo4j.driver.Values.parameters;

@ApplicationScoped
public class ObjectService {

    @Inject
    Driver driver;

    private final AtomicInteger customNodeOsmIdSupplier = new AtomicInteger(-1);

    public List<Object> findAllObjects() {
        List<Object> objects = new ArrayList<>();

        try (Session session = driver.session()) {
            List<Record> records = session.writeTransaction(tx -> {
                Result result = tx.run(
                        "MATCH (n: Object), m = (n)-[:MEMBER]-(p: Point) " +
                                "RETURN n as object, collect(p) as points"
                );

                return result.list();
            });

            objects.addAll(
                    toObjects(records)
            );
        }

        return objects;
    }

    public List<Object> findObjectsByName(String name) {
        List<Object> objects = new ArrayList<>();

        try (Session session = driver.session()) {
            List<Record> records = session.writeTransaction(tx -> {
                Result result = tx.run(
                        "MATCH (o:Object), m = (o)-[:MEMBER]-(p: Point) " +
                                "WHERE o.name CONTAINS $name " +
                                "RETURN o as object, collect(p) as points",
                        parameters("name", name)
                );

                return result.list();
            });

            objects.addAll(
                    toObjects(records)
            );
        }

        return objects;
    }

    public List<Object> findObjectsByAddress(String address) {
        List<Object> objects = new ArrayList<>();

        try (Session session = driver.session()) {
            List<Record> records = session.writeTransaction(tx -> {
                Result result = tx.run(
                        "MATCH (o:Object), m = (o)-[:MEMBER]-(p: Point) " +
                                "WHERE o.street + ' ' + o.house_number STARTS WITH $address " +
                                "RETURN o as object, collect(p) as points",
                        parameters("address", address)
                );

                return result.list();
            });

            objects.addAll(
                    toObjects(records)
            );
        }

        return objects;
    }

    static List<Object> toObjects(List<Record> records) {
        List<Object> result = new ArrayList<>();

        for (Record record : records) {
            List<Point> points = record.get("points").asList(ObjectService::mapPoint);

            Value objectValue = record.get("object");
            result.add(mapObject(objectValue, points));
        }

        return result;
    }

    static Point mapPoint(Value pointValue) {
        return new Point(
                pointValue.get("lat").asDouble(),
                pointValue.get("lon").asDouble(),
                pointValue.get("precipitation_value").asInt()
        );
    }

    static Object mapObject(Value objectValue, List<Point> points) {
        return new Object(
                objectValue.get("name").asString(),
                objectValue.get("street").asString(),
                objectValue.get("house_number").asString(),
                points
        );
    }

    public void addObject(String name, String street, String houseNumber, List<Point> points) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                List<Integer> createdPointsId = new ArrayList<>();
                for (Point point : points) {
                    int pointId = customNodeOsmIdSupplier.getAndDecrement();

                    tx.run(
                            CREATE_POINT_QUERY,
                            parameters(
                                    "osm_id", pointId,
                                    "lat", point.getLatitude(),
                                    "lon", point.getLongitude()
                            )
                    );

                    createdPointsId.add(pointId);
                }

                int objectId = customNodeOsmIdSupplier.getAndDecrement();
                tx.run(
                        CREATE_OBJECT_QUERY,
                        parameters(
                                "osm_id", objectId,
                                "name", name,
                                "street", street,
                                "house_number", houseNumber
                        )
                );

                for (Integer pointId : createdPointsId) {
                    tx.run(
                            CREATE_MEMBER_QUERY,
                            parameters(
                                    "a_osm_id", objectId,
                                    "b_osm_id", pointId
                            )
                    );
                }

                return null;
            });
        }
    }
}
