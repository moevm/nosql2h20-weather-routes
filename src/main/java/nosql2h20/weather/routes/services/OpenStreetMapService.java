package nosql2h20.weather.routes.services;

import de.topobyte.osm4j.core.access.OsmHandler;
import de.topobyte.osm4j.core.model.iface.*;
import de.topobyte.osm4j.xml.dynsax.OsmXmlReader;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.neo4j.driver.Values.parameters;

@ApplicationScoped
public class OpenStreetMapService {

    @Inject
    Driver driver;

    public void readMap(InputStream mapData) throws Exception {
        OsmXmlReader reader = new OsmXmlReader(mapData, false);
        reader.setHandler(new Neo4jHandler(driver));

        reader.read();
    }

    private static class Neo4jHandler implements OsmHandler {

        private final Driver driver;
        private final AtomicReference<Session> sessionRef = new AtomicReference<>();

        public Neo4jHandler(Driver driver) {
            this.driver = driver;
        }

        @Override
        public void handle(OsmNode node) {
            Session session = getSession();

            session.writeTransaction(tx -> tx.run(
                    "CREATE (p:Point {osm_id: $osm_id, lat: $lat, lon: $lon, precipitation_value: 0})",
                    parameters(
                            "osm_id", node.getId(),
                            "lat", node.getLatitude(),
                            "lon", node.getLongitude()
                    )
            ));
        }

        @Override
        public void handle(OsmWay way) {
            Session session = getSession();

            List<Pair<Long, Long>> nodePairs = createNodePairs(way);

            for (Pair<Long, Long> nodePair : nodePairs) {
                session.writeTransaction(tx -> tx.run(
                        "MATCH (a:Point),(b:Point) " +
                                "WHERE a.osm_id = $a_osm_id AND b.osm_id = $b_osm_id " +
                                "WITH a, b, distance(point({latitude: a.lat, longitude: a.lon}), point({latitude: b.lat, longitude: b.lon})) as d " +
                                "CREATE (a)-[r:WAY { osm_id: $osm_id, distance: d}]->(b)",
                        parameters(
                                "osm_id", way.getId(),
                                "a_osm_id", nodePair.getLeft(),
                                "b_osm_id", nodePair.getRight()
                        )
                ));
            }
        }

        private List<Pair<Long, Long>> createNodePairs(OsmWay way) {
            List<Pair<Long, Long>> nodePairs = new ArrayList<>();

            long previousNodeId = way.getNodeId(0);
            for (int nodeNum = 1; nodeNum < way.getNumberOfNodes(); nodeNum++) {
                long currentNodeId = way.getNodeId(nodeNum);
                Pair<Long, Long> nodePair = new ImmutablePair<>(previousNodeId, currentNodeId);

                nodePairs.add(nodePair);

                previousNodeId = currentNodeId;
            }

            return nodePairs;
        }

        @Override
        public void handle(OsmRelation relation) {
            Session session = getSession();

            List<OsmTag> tags = IntStream.range(0, relation.getNumberOfTags())
                    .boxed()
                    .map(relation::getTag)
                    .collect(Collectors.toList());
            Optional<OsmTag> buildingTag = findTag(tags, "building");
            Optional<OsmTag> leisureTag = findTag(tags, "leisure");
            if (buildingTag.isEmpty() && leisureTag.isEmpty()) {
                return;
            }

            Optional<OsmTag> nameTag = findTag(tags, "name");
            Optional<OsmTag> streetTag = findTag(tags, "addr:street");
            Optional<OsmTag> houseNumberTag = findTag(tags, "addr:housenumber");

            session.writeTransaction(tx -> tx.run(
                    "CREATE (c:Object {osm_id:$osm_id, name:$name, street:$street, house_number:$house_number})",
                    parameters(
                            "osm_id", relation.getId(),
                            "name", nameTag.isEmpty() ? "" : nameTag.get().getValue(),
                            "street", streetTag.isEmpty() ? "" : streetTag.get().getValue(),
                            "house_number", houseNumberTag.isEmpty() ? "" : houseNumberTag.get().getValue()
                    )
            ));


            for (int memberNum = 0; memberNum < relation.getNumberOfMembers(); memberNum++) {
                OsmRelationMember member = relation.getMember(memberNum);
                if (member.getType() != EntityType.Way) {
                    continue;
                }

                List<Record> records = session.writeTransaction(tx -> {
                    Result result = tx.run(
                            "MATCH (a:Point), (b:Point), r = (a)-[:WAY {osm_id: $osm_id}]->(b) " +
                                    "RETURN b.osm_id",
                            parameters("osm_id", member.getId())
                    );

                    return result.list();
                });

                for (Record record : records) {
                    long bOsmId = record.get("b.osm_id").asLong();
                    session.writeTransaction(tx -> tx.run(
                            "MATCH (a:Object),(b:Point) " +
                                    "WHERE a.osm_id = $a_osm_id AND b.osm_id = $b_osm_id " +
                                    "CREATE (a)-[r:MEMBER]->(b) " +
                                    "RETURN type(r)",
                            parameters(
                                    "a_osm_id", relation.getId(),
                                    "b_osm_id", bOsmId
                            )
                    ));
                }
            }
        }

        private Optional<OsmTag> findTag(List<OsmTag> allTags, String filteringKey) {
            return allTags.stream()
                    .filter(tag -> tag.getKey().equals(filteringKey))
                    .findAny();
        }

        @Override
        public void handle(OsmBounds bounds) {
        }

        private Session getSession() {
            sessionRef.compareAndSet(null, driver.session());

            Session session = sessionRef.get();
            if (!session.isOpen()) {
                session = createNewSession();
            }

            return session;
        }

        private Session createNewSession() {
            Session newSession = driver.session();
            Session prevSession = sessionRef.getAndSet(newSession);

            if (prevSession != null) {
                prevSession.close();
            }

            return newSession;
        }

        @Override
        public void complete() {
            Session session = sessionRef.get();
            if (session != null) {
                session.close();
            }
        }
    }
}
