package ododock.webserver.repository.reactive;

import ododock.webserver.domain.article.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {

    Flux<Category> findCategoryByOwnerAccountId(Long ownerAccountId);

}
