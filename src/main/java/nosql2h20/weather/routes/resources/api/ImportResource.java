package nosql2h20.weather.routes.resources.api;

import nosql2h20.weather.routes.model.FileUploadForm;
import nosql2h20.weather.routes.services.DatabaseManagementService;
import nosql2h20.weather.routes.services.OpenStreetMapService;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;

@Path("/api/import")
public class ImportResource {
    private static final Logger logger = LoggerFactory.getLogger(ImportResource.class);

    @Inject
    OpenStreetMapService osmService;
    @Inject
    DatabaseManagementService dbManagementService;

    @POST
    @Path("/map")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importMap(@MultipartForm FileUploadForm uploadForm) {
        dbManagementService.clearDatabase();

        try {
            osmService.readMap(
                    new ByteArrayInputStream(uploadForm.getData())
            );
        } catch (Exception e) {
            logger.error("Error while parsing uploaded map.", e);

            return Response.serverError().build();
        }

        return Response.ok().build();
    }
}
