package ododock.webserver.domain.article;

import lombok.RequiredArgsConstructor;
import ododock.webserver.repository.QueryCriteriaUtils;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactiveArticleQueryService implements ArticleQueryService {

    private final ReactiveMongoTemplate template;

    @Override
    public Publisher<Article> listArticles(Long accountId, ArticleListOptions listOptions) {
        return template.find(QueryCriteriaUtils.queryWith(listOptions), Article.class);
    }

}
