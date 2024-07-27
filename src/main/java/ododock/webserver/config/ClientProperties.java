package ododock.webserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "app.type")
public record ClientProperties(
        Map<String, ClientConfig> clients
) {
    public record ClientConfig(
            String baseUri,
            String key,
            String keyLabel,
            String origin
    ) {
    }
}
