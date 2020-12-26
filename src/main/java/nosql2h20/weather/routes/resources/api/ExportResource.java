package nosql2h20.weather.routes.resources.api;

import nosql2h20.weather.routes.services.DatabaseManagementService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/export")
public class ExportResource {

    @Inject
    DatabaseManagementService dbManagementService;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response export() {
        String exportedData = dbManagementService.exportDatabase();

        return Response.ok(exportedData).build();
    }
}
