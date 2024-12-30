package ododock.webserver.domain.content;

import ododock.webserver.domain.dto.PopularBookListOptions;

import java.util.List;

public class BookCurationService implements CurationService {


    @Override
    public List<Book> listPopularContents(PopularBookListOptions listOptions) {
        return List.of();
    }

    @Override
    public void listTrendingContents() {

    }

    @Override
    public void listContentsKeywordsOfMonth() {

    }

}
