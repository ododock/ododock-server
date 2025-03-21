package ododock.webserver.web.v1alpha1;

import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.curation.LibraryCurationService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.dto.curation.V1alpha1BookCurationConverter;
import ododock.webserver.web.v1alpha1.dto.curation.V1alpha1Book;
import ododock.webserver.web.v1alpha1.dto.curation.V1alpha1BookListOptions;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.CURATIONS)
public class V1alpha1CurationController {

    private final V1alpha1BookCurationConverter curationConverter;
    private final LibraryCurationService curationService;

    public V1alpha1CurationController(LibraryCurationService curationService) {
        this.curationService = curationService;
        this.curationConverter = new V1alpha1BookCurationConverter();
    }

    @GetMapping(
            value = ResourcePath.CURATIONS_SUBRESOURCE_POPULAR_BOOKS,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Flux<V1alpha1Book> listPopularBooks(V1alpha1BookListOptions listOptions) {
        return this.curationService.listPopularBook(this.curationConverter.toDomainDto(listOptions))
                .doOnNext(a -> log.info(a.toString()))
                .transform(i -> i
                        .map(this.curationConverter::toControllerDto)
                )
                .doOnNext(b -> log.info(b.toString()));
    }

}
