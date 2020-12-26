package nosql2h20.weather.routes.resources;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/route")
public class RouteResource {

    @Inject
    Template route;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance route() {
        return route.instance();
    }
}
