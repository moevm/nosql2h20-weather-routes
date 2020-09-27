package nosql2h20.weather.routes.configuration;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@SpringBootConfiguration
@PropertySource("classpath:application.yml")
public class Neo4jConfiguration {

    private final Environment environment;

    @Autowired
    public Neo4jConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean(destroyMethod = "close")
    public Driver neo4jDriver() {
        String uri = environment.getProperty("neo4j.uri");
        String user = environment.getProperty("neo4j.user");
        String password = environment.getProperty("neo4j.password");

        return GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }
}
