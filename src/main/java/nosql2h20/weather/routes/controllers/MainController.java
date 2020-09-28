package nosql2h20.weather.routes.controllers;

import nosql2h20.weather.routes.entities.Greeting;
import nosql2h20.weather.routes.services.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private final GreetingService greetingService;

    @Autowired
    public MainController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "%username%") String name) {
        return greetingService.getGreeting(name);
    }
}
