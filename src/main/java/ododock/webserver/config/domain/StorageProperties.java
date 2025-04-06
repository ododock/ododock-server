package ododock.webserver.config.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
        FileService fileService,
        S3Client s3Client
) {

    public record FileService(
            String rootDir
    ) {
    }

    public record S3Client(
            String endpoint,
            String accessKey,
            String secretKey
    ) {
    }

}
