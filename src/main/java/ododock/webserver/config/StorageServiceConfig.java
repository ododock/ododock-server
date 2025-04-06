package ododock.webserver.config;

import ododock.webserver.config.domain.StorageProperties;
import ododock.webserver.domain.FileStorageService;
import ododock.webserver.domain.S3StorageService;
import ododock.webserver.domain.StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

@Configuration
public class StorageServiceConfig {

    @Bean
    public StorageService storageService(StorageProperties storageProperties) throws IOException {
        return new FileStorageService(storageProperties.fileService().rootDir());
    }

    @Bean
    @Profile("s3")
    public StorageService s3StorageService(S3Client s3Client) throws IOException {
        return new S3StorageService("bucket-name", s3Client);
    }

}
