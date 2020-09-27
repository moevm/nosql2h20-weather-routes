package nosql2h20.weather.routes.services;

import nosql2h20.weather.routes.entities.Greeting;
import nosql2h20.weather.routes.repositories.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    private final GreetingRepository greetingRepository;

    @Autowired
    public GreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    public Greeting getGreeting(String name) {
        return greetingRepository.getGreeting(name);
    }
}
