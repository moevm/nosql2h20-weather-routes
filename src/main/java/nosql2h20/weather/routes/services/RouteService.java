package nosql2h20.weather.routes.services;

import nosql2h20.weather.routes.model.CoordinatePoint;
import nosql2h20.weather.routes.model.Point;
import nosql2h20.weather.routes.model.Route;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static org.neo4j.driver.Values.parameters;

@ApplicationScoped
public class RouteService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManagementService.class);

    @Inject
    Driver driver;

    public Route buildRoute(CoordinatePoint from, CoordinatePoint to) {
        try (Session session = driver.session()) {
            Record routeRecord = session.writeTransaction(tx -> {
                Long nearestFromId = tx.run(
                        "MATCH (a: Point) " +
                                "WITH a, distance(point({latitude: a.lat, longitude: a.lon}), point({latitude: $lat, longitude: $lon})) as d " +
                                "ORDER BY d ASC " +
                                "RETURN a.osm_id LIMIT 1",
                        parameters(
                                "lat", from.getLatitude(),
                                "lon", from.getLongitude()
                        )
                ).single().get("a.osm_id").asLong();

                Long nearestToId = tx.run(
                        "MATCH (a: Point) " +
                                "WITH a, distance(point({latitude: a.lat, longitude: a.lon}), point({latitude: $lat, longitude: $lon})) as d " +
                                "ORDER BY d ASC " +
                                "RETURN a.osm_id LIMIT 1",
                        parameters(
                                "lat", to.getLatitude(),
                                "lon", to.getLongitude()
                        )
                ).single().get("a.osm_id").asLong();

                return tx.run(
                        "MATCH (a:Point {osm_id:$from_id} ), (b:Point {osm_id: $to_id}), p=shortestPath((a)-[*]-(b)) " +
                                "WITH nodes(p) AS ps, p " +
                                "WHERE ALL(r IN relationships(p) WHERE endNode(r).precipitation_value < 200 AND type(r) = 'WAY') " +
                                "RETURN ps, reduce(totalDistance = 0, x in relationships(p)| totalDistance + x.distance) as dist, reduce(totalPV = 0, x in ps | totalPV + x.precipitation_value)/SIZE(ps) as precipitation_avg " +
                                "ORDER BY dist ASC LIMIT 1",
                        parameters(
                                "from_id", nearestFromId,
                                "to_id", nearestToId
                        )
                ).single();
            });

            List<Point> points = routeRecord.get("points").asList(ObjectService::mapPoint);
            double totalDistance = routeRecord.get("dist").asDouble();
            int precipitationAvg = routeRecord.get("precipitation_avg").asInt();

            Route route = toRoute(points, totalDistance, precipitationAvg);

            logger.debug("Route was successfully build. Route: {}", route);

            return route;
        }
    }

    private static Route toRoute(List<Point> points, double totalDistance, int precipitationAvg) {
        List<CoordinatePoint> coordinatePoints = points.stream()
                .map(point -> new CoordinatePoint(point.getLatitude(), point.getLongitude()))
                .collect(Collectors.toList());

        return new Route(coordinatePoints, totalDistance, precipitationAvg);
    }
}
