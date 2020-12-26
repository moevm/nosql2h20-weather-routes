package nosql2h20.weather.routes.resources.api;

import nosql2h20.weather.routes.model.Route;
import nosql2h20.weather.routes.model.requests.FindRouteRequest;
import nosql2h20.weather.routes.services.RouteService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/route/find")
public class FindRouteResource {

    @Inject
    RouteService routeService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRoute(FindRouteRequest request) {
        Route route = routeService.buildRoute(request.getFrom(), request.getTo());
        if (route == null) {
            return Response.serverError().build();
        }

        return Response.ok(route).build();
    }
}
