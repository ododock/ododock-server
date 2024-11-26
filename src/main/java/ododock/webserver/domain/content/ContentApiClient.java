package ododock.webserver.domain.content;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class ContentApiClient {

    private final WebClient webClient;
    private final String baseUrl;
    private final String serviceToken;

    

}
