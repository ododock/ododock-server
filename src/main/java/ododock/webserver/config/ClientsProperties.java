package ododock.webserver.config;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

@ConfigurationProperties(prefix = "app.client.type")
public class ClientsProperties {

    @Getter
    @NestedConfigurationProperty
    private List<ClientConfig> clients;

    public record ClientConfig(
            String baseUri,
            String key,
            String keyLabel,
            String origin
    ) {
    }

}
