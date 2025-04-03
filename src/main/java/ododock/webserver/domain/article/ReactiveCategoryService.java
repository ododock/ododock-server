package ododock.webserver.domain.article;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.Account;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.repository.reactive.ArticleRepository;
import ododock.webserver.repository.reactive.CategoryRepository;
import ododock.webserver.web.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@AllArgsConstructor
public class ReactiveCategoryService implements CategoryService {

    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    @Override
    public Mono<Category> getCategory(String categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public Flux<Category> listCategoriesByOwnerAccountId(Long accountId) {
        return Mono.fromCallable(() -> accountRepository.existsById(accountId))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(exists -> {
                    if (!exists) {
                        return Flux.error(new ResourceNotFoundException(Account.class, accountId));
                    }
                    return categoryRepository.findCategoryByOwnerAccountId(accountId);
                });
    }

    @Override
    public Mono<Category> createCategory(Category category) {
        return Mono.fromCallable(() -> accountRepository.existsById(category.getOwnerAccountId()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new ResourceNotFoundException(Account.class, category.getOwnerAccountId()));
                    }
                    return categoryRepository.save(category);
                });
    }

    @Override
    public Mono<Category> updateCategory(String id, Category request) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(Category.class, id))) // 존재하지 않으면 예외 발생
                .flatMap(existingCategory -> {
                    existingCategory.updateName(request.getName());
                    existingCategory.updatePosition(request.getPosition());
                    existingCategory.updateVisibility(request.isVisibility());
                    return categoryRepository.save(existingCategory); // 저장 후 반환
                });
    }

    @Override
    public Mono<Void> deleteCategory(final String categoryId) {
        return categoryRepository.findById(categoryId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(Category.class, categoryId)))
                .flatMap(category -> categoryRepository.deleteById(categoryId))
                .thenMany(articleRepository.findArticlesByCategoryId(categoryId))
                .flatMap(article -> {
                    article.updateCategory(null);
                    return articleRepository.save(article);
                })
                .then();
    }

}
