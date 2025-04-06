package ododock.webserver.domain.article;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.concurrent.Flow;

public interface CategoryService {
    Publisher<Category> getCategory(String categoryId);

    Publisher<Category> listCategoriesByOwnerAccountId(Long accountId);

    Publisher<Category> createCategory(Category category);

    Publisher<Category> updateCategory(String id, Category request);

    Publisher<Void> deleteCategory(String id);
}