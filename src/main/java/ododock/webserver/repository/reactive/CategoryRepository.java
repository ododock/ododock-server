package ododock.webserver.repository.reactive;

import ododock.webserver.domain.article.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String>, ReactiveCategoryRepository {

    Flux<Category> findCategoryByOwnerAccountId(Long ownerAccountId, Sort sort);

    Mono<Category> findCategoryByOwnerAccountIdAndPosition(Long ownerAccountId, Integer position);

    Mono<Long> countCategoriesByOwnerAccountId(Long ownerAccountId);

}
