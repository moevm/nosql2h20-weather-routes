package nosql2h20.weather.routes.resources.api;

import nosql2h20.weather.routes.model.FileUploadForm;
import nosql2h20.weather.routes.services.DatabaseService;
import nosql2h20.weather.routes.services.OpenStreetMapService;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Path("/api/db")
public class DatabaseResource {
    private static final Logger logger = Logger.getLogger(DatabaseResource.class);

    public static final String SAVED_MAP_PATH = "/home/la_sk/IdeaProjects/nosql2h20-weather-routes/src/main/resources/map.osm";

    @Inject
    OpenStreetMapService osmService;

    @Inject
    DatabaseService dbService;

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadMap(@MultipartForm FileUploadForm uploadForm) {
        File map;
        try {
            map = writeFile(SAVED_MAP_PATH, uploadForm.getData());
        } catch (IOException e) {
            logger.error("Unable to save uploaded map.", e);

            return Response.serverError().build();
        }

        try {
            osmService.readMap(map);
        } catch (Exception e) {
            logger.error("Unable to read loaded map.", e);

            return Response.serverError().build();
        }

        return Response.ok().build();
    }

    private File writeFile(String name, byte[] data) throws IOException {
        File file = new File(name);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(data);
        fop.flush();
        fop.close();

        return file;
    }

    @GET
    @Path("/drop")
    @Produces(MediaType.APPLICATION_JSON)
    public Response dropDB() {
        dbService.dropDatabase();

        return Response.ok().build();
    }
}
