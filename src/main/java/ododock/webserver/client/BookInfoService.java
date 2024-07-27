package ododock.webserver.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.config.ClientProperties;
import ododock.webserver.domain.content.Content;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookInfoService implements ContentQueryService {

    private final Map<String, ClientProperties.ClientConfig> clientConfigs;

    @Override
    public Content search(@Nullable String keyword) {
        WebClient client = WebClient.create(clientConfigs.get("book-info").baseUri());
        Mono<String> result = client.get()
                .uri("some")
                .retrieve()
                .bodyToMono(String.class);

        result.subscribe(response -> {
            System.out.println("response: " + response);
        });


        return null;
    }

}
