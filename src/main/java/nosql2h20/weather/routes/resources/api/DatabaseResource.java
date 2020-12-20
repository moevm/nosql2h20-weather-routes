package nosql2h20.weather.routes.resources.api;

import nosql2h20.weather.routes.model.FileUploadForm;
import nosql2h20.weather.routes.services.DatabaseManagementService;
import nosql2h20.weather.routes.services.MapFileService;
import nosql2h20.weather.routes.services.OpenStreetMapService;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Path("/api/database")
public class DatabaseResource {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseResource.class);

    @Inject
    MapFileService mapFileService;
    @Inject
    OpenStreetMapService osmService;
    @Inject
    DatabaseManagementService dbManagementService;

    @GET
    @Path("/map")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableMaps() {
        List<String> mapFiles;
        try {
            mapFiles = mapFileService.findAllSavedMaps();
        } catch (IOException e) {
            logger.error("Error while finding available maps occurred.", e);

            return Response.serverError().build();
        }

        return Response.ok(mapFiles).build();
    }

    @GET
    @Path("/map/upload/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadMap(@PathParam("name") String name) {
        File map;

        try {
            map = mapFileService.getMapFile(name);
        } catch (IOException e) {
            logger.error("Error while getting map file occurred. Name: {}", name, e);

            return Response.serverError().build();
        }

        dbManagementService.clearDatabase();

        try {
            osmService.readMap(map);
        } catch (Exception e) {
            logger.error("Error while parsing uploaded map.", e);

            return Response.serverError().build();
        }

        return Response.ok().build();
    }

    @POST
    @Path("/map/upload/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadMap(@MultipartForm FileUploadForm uploadForm) {
        File map;
        try {
            map = mapFileService.writeMapToFile(uploadForm.getData());
        } catch (IOException e) {
            logger.error("Error while saving uploaded map.", e);

            return Response.serverError().build();
        }

        dbManagementService.clearDatabase();

        try {
            osmService.readMap(map);
        } catch (Exception e) {
            logger.error("Error while parsing uploaded map.", e);

            return Response.serverError().build();
        }

        return Response.ok().build();
    }
}
