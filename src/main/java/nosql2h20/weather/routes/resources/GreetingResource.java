package nosql2h20.weather.routes.resources;

import nosql2h20.weather.routes.entities.Greeting;
import nosql2h20.weather.routes.services.GreetingService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/greeting")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    @Inject
    GreetingService greetingService;

    @GET
    public Response greeting(@QueryParam("name") @DefaultValue("%username%") String name) {
        Greeting greeting = greetingService.getGreeting(name);

        return Response.ok(greeting).build();
    }
}
