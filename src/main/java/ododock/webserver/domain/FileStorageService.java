package ododock.webserver.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileStorageService implements StorageService {

    private final Path fileStoragePath;

    public FileStorageService(String rootDir) throws IOException {
        this.fileStoragePath = Path.of(rootDir).toAbsolutePath();
        if (!Files.exists(fileStoragePath)) {
            Files.createDirectories(fileStoragePath);
        }
    }

    @Override
    public byte[] getData(String basePath, String filename) {
        try {
            Path filePath = fileStoragePath.resolve(basePath).resolve(filename);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: {} at {}" + filename + basePath, e);
        }
    }

    @Override
    public String saveData(String filename, byte[] file) throws IOException {
        Path targetPath = fileStoragePath.resolve(filename);
        if (!Files.exists(targetPath)) {
            Files.createFile(targetPath);
        }
        Files.write(targetPath, file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return targetPath.toString();
    }

    @Override
    public String updateData() {
        // todo implement if need
        return "";
    }

    @Override
    public void deleteData(String basePath, String filename) throws IOException {
        Path targetDir = fileStoragePath.resolve(basePath);
        if (!Files.exists(targetDir)) {
            return;
        }
        Path filePath = targetDir.resolve(filename);
        Files.deleteIfExists(filePath);
    }

}
