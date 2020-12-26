package nosql2h20.weather.routes.services;

public class Queries {
    public static final String EXPORT_DATA_QUERY = "CALL apoc.export.graphml.all(null, {stream:true}) " +
            "YIELD file, nodes, relationships, properties, data " +
            "RETURN nodes, relationships, properties, data";

    public static final String CLEAR_DATABASE_QUERY = "MATCH (n) " +
            "DETACH DELETE n " +
            "RETURN count(n) as deleted";

    public static final String CREATE_POINT_QUERY = "CREATE (p:Point {osm_id: $osm_id, lat: $lat, lon: $lon, precipitation_value: 0})";

    public static final String CREATE_WAY_QUERY = "MATCH (a:Point),(b:Point) " +
            "WHERE a.osm_id = $a_osm_id AND b.osm_id = $b_osm_id " +
            "WITH a, b, distance(point({latitude: a.lat, longitude: a.lon}), point({latitude: b.lat, longitude: b.lon})) as d " +
            "CREATE (a)-[r:WAY { osm_id: $osm_id, distance: d}]->(b)";

    public static final String CREATE_OBJECT_QUERY = "CREATE (c:Object {osm_id:$osm_id, name:$name, street:$street, house_number:$house_number})";

    public static final String FIND_WAY_POINTS_QUERY = "MATCH (a:Point), (b:Point), r = (a)-[:WAY {osm_id: $osm_id}]->(b) " +
            "UNWIND nodes(r) as point RETURN DISTINCT point";

    public static final String CREATE_MEMBER_QUERY = "MATCH (a:Object),(b:Point) " +
            "WHERE a.osm_id = $a_osm_id AND b.osm_id = $b_osm_id " +
            "CREATE (a)-[r:MEMBER]->(b)";

    public static final String FIND_NEAREST_POINT_QUERY = "MATCH (a: Point) " +
            "WITH a, distance(point({latitude: a.lat, longitude: a.lon}), point({latitude: $lat, longitude: $lon})) as d " +
            "ORDER BY d ASC " +
            "RETURN a.osm_id LIMIT 1";

    public static final String FIND_ROUTE_QUERY = "MATCH (a:Point {osm_id:$from_id} ), (b:Point {osm_id: $to_id}), p=shortestPath((a)-[:WAY*]-(b)) " +
            "UNWIND nodes(p) AS points " +
            "RETURN points, " +
            "reduce(totalDistance = 0, x in relationships(p)| totalDistance + x.distance) as dist, " +
            "reduce(totalPV = 0, x in nodes(p) | totalPV + x.precipitation_value) / SIZE(nodes(p)) as precipitation_avg";
}
