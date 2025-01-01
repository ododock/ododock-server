package ododock.webserver.domain.curation;

import lombok.extern.slf4j.Slf4j;
import ododock.webserver.config.domain.LibraryCurationClientProperties;
import ododock.webserver.domain.curation.dto.LibraryBook;
import ododock.webserver.domain.curation.dto.LibraryResponse;
import ododock.webserver.domain.curation.dto.LibraryBookList;
import ododock.webserver.domain.curation.dto.LibraryBookListOptions;
import ododock.webserver.web.v1alpha1.dto.curation.V1alpha1BookCurationConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Service
public class LibraryCurationService implements CurationService {

    private final WebClient curationWebClient;
    private final LibraryBookListOptionsUtil listOptionsUtil;
    private final LibraryCurationClientProperties clientProperties;
    private final V1alpha1BookCurationConverter curationConverter;

    public LibraryCurationService(LibraryCurationClientProperties clientProperties, WebClient curationWebClient) {
        this.listOptionsUtil = new LibraryBookListOptionsUtil(clientProperties.apiKey());
        this.clientProperties = clientProperties;
        this.curationWebClient = curationWebClient;
        this.curationConverter = new V1alpha1BookCurationConverter();
    }

    @Override
    public Flux<LibraryBook> listPopularContents(LibraryBookListOptions listOptions) {
        LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        this.listOptionsUtil.applyPopularBookListOptions(this.clientProperties, queryParams, listOptions);

        return this.curationWebClient.get()
                .uri(uriBuilder -> {
                    URI finalUri = uriBuilder
                            .queryParams(queryParams)
                            .build();
                    log.info("Base URL: {}", uriBuilder.build().toString());
                    log.info("Query Parameters: {}", queryParams);
                    log.info("Final URI: {}", finalUri);
                    return finalUri;
                })
                .retrieve()
                .bodyToMono(LibraryResponse.class)
                .doOnNext(res -> {
                    log.info("Received response: {}", res);
                })
                .onErrorResume(error -> {
                    log.error("Error fetching popular books: ", error);
                    if (error instanceof AsyncRequestTimeoutException) {
                        log.error("Request timed out");
                    }
                    return Mono.error(error);
                }).map(i -> i.response()
                        .docs().stream()
                        .map(LibraryBookList.PopularBookDoc::doc)
                        .toList())
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public void listTrendingContents() {

    }

    @Override
    public void listContentsKeywordsOfMonth() {

    }

}
