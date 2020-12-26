package nosql2h20.weather.routes.resources.api;

import nosql2h20.weather.routes.model.Object;
import nosql2h20.weather.routes.model.requests.AddObjectRequest;
import nosql2h20.weather.routes.services.ObjectService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/object")
public class ObjectResource {

    @Inject
    ObjectService objectService;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllObjects() {
        List<Object> objects = objectService.findAllObjects();

        return Response.ok(objects).build();
    }

    @GET
    @Path("/filter/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjectByName(@PathParam("name") String name) {
        List<Object> objects = objectService.findObjectsByName(name);

        return Response.ok(objects).build();
    }

    @GET
    @Path("/filter/address/{address}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjectByAddress(@PathParam("address") String address) {
        List<Object> objects = objectService.findObjectsByAddress(address);

        return Response.ok(objects).build();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addObject(AddObjectRequest request) {
        objectService.addObject(
                request.getName(),
                request.getStreet(),
                request.getHouseNumber(),
                request.getPoints()
        );

        return Response.ok().build();
    }
}
