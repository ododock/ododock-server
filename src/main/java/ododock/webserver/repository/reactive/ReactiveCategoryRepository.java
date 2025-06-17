package ododock.webserver.repository.reactive;

import ododock.webserver.domain.article.Category;
import reactor.core.publisher.Flux;

public interface ReactiveCategoryRepository {

    Flux<Category> findCategoriesByRange(Long ownerAccountId, Integer rangeStart, Integer rangeEnd);

}
