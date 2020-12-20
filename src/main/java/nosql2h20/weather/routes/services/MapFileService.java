package nosql2h20.weather.routes.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class MapFileService {
    private static final Logger logger = LoggerFactory.getLogger(MapFileService.class);

    public List<String> findAllSavedMaps() throws IOException {
        String rootPath = getMapRootDirPath();

        try (Stream<Path> pathStream = Files.walk(Paths.get(rootPath))) {
            return pathStream.filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        }
    }

    public File getMapFile(String name) throws IOException {
        String rootPath = getMapRootDirPath();

        try (Stream<Path> pathStream = Files.walk(Paths.get(rootPath))) {
            return pathStream
                    .filter(path -> path.getFileName().toString().equals(name))
                    .findAny()
                    .orElseThrow(() -> new IOException("Unable to find map file " + name))
                    .toFile();
        }
    }

    public File writeMapToFile(byte[] mapData) throws IOException {
        String rootPath = getMapRootDirPath();

        String timestampSuffix = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")
                .format(new Date());
        File mapFile = new File(
                String.format("%s/map-%s.osm", rootPath, timestampSuffix)
        );

        if (!mapFile.createNewFile()) {
            throw new IOException("Unable to create file: " + mapFile.getName());
        }

        FileOutputStream fop = new FileOutputStream(mapFile);

        fop.write(mapData);
        fop.flush();
        fop.close();

        logger.debug("Successfully wrote map to file: {}.", mapFile.getName());

        return mapFile;
    }

    private String getMapRootDirPath() throws IOException {
        URL mapDir = MapFileService.class.getClassLoader().getResource("maps");
        if (mapDir == null) {
            throw new IOException("Unable to find maps root dir.");
        }

        return mapDir.getPath();
    }
}
