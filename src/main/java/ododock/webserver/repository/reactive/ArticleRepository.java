package ododock.webserver.repository.reactive;

import ododock.webserver.domain.article.Article;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ArticleRepository extends ReactiveMongoRepository<Article, String> {

    Flux<Article> findArticlesByOwnerAccountId(Long ownerAccountId);

    Flux<Article> findArticlesByCategoryId(String categoryId);

}
