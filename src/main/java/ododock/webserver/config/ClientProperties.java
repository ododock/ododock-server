package ododock.webserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "app.client.type")
public record ClientProperties(
        @NestedConfigurationProperty
        List<ClientConfig> clients
) {
    public record ClientConfig(
            String baseUri,
            String key,
            String keyLabel,
            String origin
    ) {
    }
}
