package ododock.webserver.domain.article;

import lombok.RequiredArgsConstructor;
import ododock.webserver.repository.QueryCriteriaUtils;
import org.apache.coyote.BadRequestException;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReactiveArticleQueryService implements ArticleQueryService {

    private final ReactiveMongoTemplate template;

    @Override
    public Publisher<Article> listArticles(Long accountId, ArticleListOptions listOptions) throws BadRequestException {
        return template.find(QueryCriteriaUtils.queryWith(listOptions), Article.class)
                .onErrorResume(Mono::error);
    }

}
