package ododock.webserver.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(ClientsProperties.class)
@RequiredArgsConstructor
public class ClientsConfig {

    private final List<ClientsProperties> clientsProperties;

    @Bean
    public ClientsProperties.ClientConfig bookInfoConfig() {
//        clientsProperties.stream()

        return clientsProperties.clients().get("book-info");

    }

    @Bean
    public ClientsProperties.ClientConfig bookTrendConfig() {
        return clientsProperties.clients().get("book-trend");
    }

    @Bean
    public ClientsProperties.ClientConfig movieInfoConfig() {
        return clientsProperties.clients().get("movie-info");
    }

}
