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

import static nosql2h20.weather.routes.services.Queries.FIND_NEAREST_POINT_QUERY;
import static nosql2h20.weather.routes.services.Queries.FIND_ROUTE_QUERY;
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
                        FIND_NEAREST_POINT_QUERY,
                        parameters(
                                "lat", from.getLatitude(),
                                "lon", from.getLongitude()
                        )
                ).single().get("a.osm_id").asLong();

                Long nearestToId = tx.run(
                        FIND_NEAREST_POINT_QUERY,
                        parameters(
                                "lat", to.getLatitude(),
                                "lon", to.getLongitude()
                        )
                ).single().get("a.osm_id").asLong();

                return tx.run(
                        FIND_ROUTE_QUERY,
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
