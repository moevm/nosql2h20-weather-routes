package nosql2h20.weather.routes.repositories;

import nosql2h20.weather.routes.entities.Greeting;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.neo4j.driver.Values.parameters;

@Repository
public class GreetingRepository {

    private final Driver neo4jDriver;

    @Autowired
    public GreetingRepository(Driver neo4jDriver) {
        this.neo4jDriver = neo4jDriver;
    }

    public Greeting getGreeting(String name) {
        try (Session session = neo4jDriver.session()) {
            String nodeGreeting = session.writeTransaction(transaction -> {
                Result result = transaction.run("CREATE (a:Greeting) " +
                                "SET a.name = $name " +
                                "RETURN 'Hello, ' + a.name + '!\nBest wishes from node ' + id(a) + '.'",
                        parameters("name", name));

                return result.single().get(0).asString();
            });

            return new Greeting(nodeGreeting);
        }
    }
}
