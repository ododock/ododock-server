package ododock.webserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(ClientProperties.class)
@RequiredArgsConstructor
public class ClientsConfiguration {

    private final List<ClientProperties> clientProperties;

    @Bean
    public ClientProperties.ClientConfig bookInfoConfig() {
//        clientProperties.stream()

        return clientProperties.clients().get("book-info");

    }

    @Bean
    public ClientProperties.ClientConfig bookTrendConfig() {
        return clientProperties.clients().get("book-trend");
    }

    @Bean
    public ClientProperties.ClientConfig movieInfoConfig() {
        return clientProperties.clients().get("movie-info");
    }

}
