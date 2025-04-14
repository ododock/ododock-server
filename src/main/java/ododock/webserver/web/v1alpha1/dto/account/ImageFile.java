package ododock.webserver.web.v1alpha1.dto.account;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record ImageFile(
        String filename,
        String extension,
        byte[] data
) {
    public static ImageFile from(MultipartFile file) throws IOException {
        return new ImageFile(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );
    }

    public String getFileExtension() {
        return extension.split("/")[1];
    }

}
