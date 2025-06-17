package ododock.webserver.repository.reactive;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.article.Category;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Repository
public class ReactiveCategoryRepositoryImpl implements ReactiveCategoryRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Category> findCategoriesByRange(Long ownerAccountId, Integer rangeStart, Integer rangeEnd) {
        Query query = new Query(
                Criteria.where("ownerAccountId").is(ownerAccountId)
                        .and("position").gte(rangeStart).lte(rangeEnd)
        );
        return mongoTemplate.find(query, Category.class);
    }

}
