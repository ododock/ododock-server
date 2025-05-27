package ododock.webserver.domain;

import java.io.IOException;

public interface StorageService {

    byte[] getData(String id, String filename) throws IOException;

    String saveData(String filename, byte[] file) throws IOException;

    String updateData();

    void deleteData(String filePath) throws IOException;

}
