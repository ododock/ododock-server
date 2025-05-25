package ododock.webserver.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class FileStorageService implements StorageService {

    private final Path fileStoragePath;

    public FileStorageService(String rootDir) throws IOException {
        if (rootDir == null || rootDir.isBlank()) {
            throw new IllegalArgumentException("Root directory must not be null or empty");
        }
        Path directoryPath = Paths.get(rootDir);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
        fileStoragePath = directoryPath;
    }

    @Override
    public byte[] getData(String basePath, String filename) {
        try {
            // __ todo add exception handling when file not exists even db store filepath
            Path filePath = fileStoragePath.resolve(basePath).resolve(filename);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: {} at {}" + filename + basePath, e);
        }
    }

    @Override
    public String saveData(String filename, byte[] file) throws IOException {
        Path targetPath = fileStoragePath.resolve(filename);
        ensureDirectoryExists(targetPath);

        Files.write(targetPath, file, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        return String.format("/%s/%s", fileStoragePath.getFileName(), filename);
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

    private void ensureDirectoryExists(Path targetPath) throws IOException {
        Path parentDir = targetPath.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }
        if (Files.exists(targetPath)) {
            Files.deleteIfExists(targetPath);
        }
    }

}
