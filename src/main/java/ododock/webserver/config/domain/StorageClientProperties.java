package ododock.webserver.config.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.clients.storage")
public record StorageClientProperties(
        String s3Endpoint,
        String s3AccessKey,
        String s3SecretKey
) {
}
