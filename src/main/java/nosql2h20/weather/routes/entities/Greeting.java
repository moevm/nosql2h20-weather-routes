package nosql2h20.weather.routes.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public class Greeting {

    public Long id;
    public String content;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public Greeting(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public static Greeting from(Node node) {
        return new Greeting(node.id(), node.get("content").asString());
    }
}
