package ododock.webserver.domain.article;

import lombok.AllArgsConstructor;
import ododock.webserver.domain.account.Account;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.repository.reactive.ArticleRepository;
import ododock.webserver.repository.reactive.CategoryRepository;
import ododock.webserver.web.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
                    Sort sort = Sort.by(Sort.Direction.ASC, "position");
                    return categoryRepository.findCategoryByOwnerAccountId(accountId, sort);
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
                    return categoryRepository.countCategoriesByOwnerAccountId(category.getOwnerAccountId())
                            .flatMap(count -> {
                                category.updatePosition(Math.toIntExact(count));
                                return categoryRepository.save(category);
                            });
                });
    }

    @Override
    public Mono<Category> updateCategory(Long ownerId, Category request) {
        return categoryRepository.findById(request.getId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(Category.class, request.getId())))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(existingCategory -> {
                    existingCategory.updateName(request.getName());
                    existingCategory.updateVisibility(request.isVisibility());

                    if (!request.getPosition().equals(existingCategory.getPosition())) {
                        return handlePositionUpdate(request, existingCategory);
                    }

                    return categoryRepository.save(existingCategory);
                });
    }

    private Mono<Category> handlePositionUpdate(Category request, Category existingCategory) {
        if (request.getPosition() > existingCategory.getPosition()) {
            Mono<Category> target = this.categoryRepository.findCategoryByOwnerAccountIdAndPosition(request.getOwnerAccountId(), request.getPosition());
            return target
                    .flatMap(category -> {
                        category.updatePosition(existingCategory.getPosition());
                        return this.categoryRepository.save(category);
                    })
                    .then(Mono.defer(() -> {
                        existingCategory.updatePosition(request.getPosition());
                        return this.categoryRepository.save(existingCategory);
                    }));
        }
        return categoryRepository.findCategoriesByRange(request.getOwnerAccountId(), request.getPosition(), existingCategory.getPosition())
                .flatMap(targetCategory -> {
                    if (!targetCategory.getId().equals(request.getId())
                            && targetCategory.getPosition() <= existingCategory.getPosition()) {
                        targetCategory.shiftPositionRight();
                        return categoryRepository.save(targetCategory);
                    }
                    return Mono.empty();
                })
                .then(Mono.defer(() -> {
                    existingCategory.updatePosition(request.getPosition());
                    return categoryRepository.save(existingCategory);
                }));
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
