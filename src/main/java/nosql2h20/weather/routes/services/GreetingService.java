package nosql2h20.weather.routes.services;

import nosql2h20.weather.routes.entities.Greeting;
import nosql2h20.weather.routes.repositories.GreetingRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GreetingService {

    @Inject
    GreetingRepository greetingRepository;

    public Greeting getGreeting(String name) {
        return greetingRepository.getGreeting(name);
    }
}
