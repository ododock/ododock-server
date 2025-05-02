package ododock.webserver.domain.article;

import org.reactivestreams.Publisher;

public interface CategoryService {

    Publisher<Category> getCategory(String categoryId);

    Publisher<Category> listCategoriesByOwnerAccountId(Long accountId);

    Publisher<Category> createCategory(Category category);

    Publisher<Category> updateCategory(Long ownerId, Category category);

    Publisher<Void> deleteCategory(String id);

}
