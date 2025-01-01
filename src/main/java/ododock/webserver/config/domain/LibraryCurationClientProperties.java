package ododock.webserver.config.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.clients.curation")
public record LibraryCurationClientProperties(
        String baseUrl,
        String apiKey
) {

}
