package ododock.webserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.client.content")
@RequiredArgsConstructor
public class ContentClientProperties {

    private final String baseUrl;
    private final String apiKey;



}
