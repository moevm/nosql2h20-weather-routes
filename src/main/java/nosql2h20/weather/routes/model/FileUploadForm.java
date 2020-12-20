package nosql2h20.weather.routes.model;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;

public class FileUploadForm {

    private byte[] data;

    public FileUploadForm() {
    }

    public byte[] getData() {
        return data;
    }

    @FormParam("uploadedFile")
    @PartType("application/octet-stream")
    public void setData(byte[] data) {
        this.data = data;
    }
}
