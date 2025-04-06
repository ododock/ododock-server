package ododock.webserver.domain;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    byte[] getData(String id, String filename);

    void saveData(String id, String filename, MultipartFile file) throws IOException;

    void updateData();

    void deleteData();

}
