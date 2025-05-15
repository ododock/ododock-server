package ododock.webserver.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;

import java.util.List;

@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(
        @Nullable
        List<String> allowedOrigins
) {
}
