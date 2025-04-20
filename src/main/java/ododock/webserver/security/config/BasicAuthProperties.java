package ododock.webserver.security.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.security.basic-auth")
public class BasicAuthProperties {

    @Getter
    private final List<BasicUser> users = new ArrayList<>();

    @AllArgsConstructor
    public static class BasicUser {
        @Getter
        private String username;
        @Getter
        private String password;
    }

}
