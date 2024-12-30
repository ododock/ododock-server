package ododock.webserver.domain.content;

import ododock.webserver.domain.dto.PopularBookListOptions;

import java.util.List;

public interface CurationService {

    List<Book> listPopularContents(PopularBookListOptions listOptions);

    void listTrendingContents();

    void listContentsKeywordsOfMonth();

}
