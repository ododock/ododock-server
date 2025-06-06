package ododock.webserver.web.v1alpha1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.article.CategoryService;
import ododock.webserver.repository.reactive.ArticleRepository;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.dto.article.V1alpha1ArticleSummary;
import ododock.webserver.web.v1alpha1.dto.category.V1alpha1Category;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_CATEGORIES)
@RequiredArgsConstructor
public class V1alpha1CategoryController {

    private final ArticleRepository articleRepository;
    private final CategoryService categoryService;

    @GetMapping(
            value = "")
    public Publisher<V1alpha1Category> listCategoriesByAccountId(
            final @PathVariable Long id) {
        // 'id' is accountId
        return Flux.defer(() -> Flux.from(categoryService.listCategoriesByOwnerAccountId(id))
                .flatMap(category -> {
                    V1alpha1Category dto = V1alpha1Category.toControllerDto(category);
                    return articleRepository.findArticlesByCategoryId(category.getId())
                            .collectList()
                            .map(articles -> {
                                // todo refactor merge into dto converting process
                                List<V1alpha1ArticleSummary> summaries = articles.stream()
                                        .map(V1alpha1ArticleSummary::toControllerDto)
                                        .toList();
                                dto.setArticleSummaries(summaries);
                                return dto;
                            });
                })
        );
    }

    @GetMapping(
            value = "/{" + ResourcePath.PATH_VAR_SUB_ID + "}")
    public Publisher<V1alpha1Category> getCategoryById(
            final @PathVariable String id,
            final @PathVariable String subId) {
        // 'id' is accountId
        return Flux.from(categoryService.getCategory(subId))
                .map(V1alpha1Category::toControllerDto);
    }

    @PostMapping(
            value = "")
    public Publisher<V1alpha1Category> createCategory(
            final @PathVariable Long id,
            final @Valid @RequestBody V1alpha1Category category) {
        // 'id' is accountId
        return Mono.from(categoryService.createCategory(category.toDomainDto()))
                .map(V1alpha1Category::toControllerDto);
    }

    @PatchMapping(
            value = "/{" + ResourcePath.PATH_VAR_SUB_ID + "}")
    public Publisher<V1alpha1Category> updateCategory(
            final @PathVariable Long id,
            final @PathVariable String subId,
            final @Valid @RequestBody V1alpha1Category category
    ) {
        // id is accountId
        // subId is categoryId
        return Mono.from(categoryService.updateCategory(id, category.toDomainDto()))
                .map(V1alpha1Category::toControllerDto);
    }

    @DeleteMapping(
            value = "/{" + ResourcePath.PATH_VAR_SUB_ID + "}")
    public Publisher<Void> deleteCategory(
            final @PathVariable Long id,
            final @PathVariable String subId) {
        // id is accountId
        // subId is categoryId
        // todo validate account existence
        // todo validate category existence
        return categoryService.deleteCategory(subId);
    }

}
