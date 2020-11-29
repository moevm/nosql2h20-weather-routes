package nosql2h20.weather.routes.repositories;

import nosql2h20.weather.routes.entities.Greeting;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.neo4j.driver.Values.parameters;

@ApplicationScoped
@Transactional
public class GreetingRepository {

    @Inject
    Driver driver;

    public Greeting getGreeting(String name) {
        try (Session session = driver.session()) {
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
