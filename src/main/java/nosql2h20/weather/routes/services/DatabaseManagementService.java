package nosql2h20.weather.routes.services;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DatabaseManagementService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManagementService.class);

    @Inject
    Driver driver;

    public void clearDatabase() {
        try (Session session = driver.session()) {
            Record record = session.writeTransaction(tx ->
                    tx.run(
                            "MATCH (n) " +
                                    "DETACH DELETE n " +
                                    "RETURN count(n) as deleted"
                    ).single()
            );

            logger.debug("Database was successfully cleared. Removed {} objects.", record.get("deleted").asLong());
        }
    }
}
