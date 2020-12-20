package nosql2h20.weather.routes.services;

import org.jboss.logging.Logger;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DatabaseManagementService {
    private static final Logger logger = Logger.getLogger(DatabaseManagementService.class);

    @Inject
    Driver driver;

    public void dropDatabase() {
        try (Session session = driver.session()) {
            session.writeTransaction(
                    tx -> tx.run("MATCH (n) DETACH DELETE n")
            );
        }
    }
}
