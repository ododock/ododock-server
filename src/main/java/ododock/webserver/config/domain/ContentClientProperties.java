package ododock.webserver.config.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.clients.book-contents")
public record ContentClientProperties(
        String baseUrl,
        String apiKey
) {

}
