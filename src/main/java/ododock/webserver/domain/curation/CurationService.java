package ododock.webserver.domain.curation;

import ododock.webserver.domain.curation.dto.LibraryBook;
import ododock.webserver.domain.curation.dto.LibraryBookListOptions;
import reactor.core.publisher.Flux;

public interface CurationService {

    Flux<LibraryBook> listPopularContents(LibraryBookListOptions listOptions);

    void listTrendingContents();

    void listContentsKeywordsOfMonth();

}
