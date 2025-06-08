package ododock.webserver.domain.article;

import org.apache.coyote.BadRequestException;
import org.reactivestreams.Publisher;

public interface ArticleQueryService {

    Publisher<Article> listArticles(Long accountId, ArticleListOptions listOptions) throws BadRequestException;

}
