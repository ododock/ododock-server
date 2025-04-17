package ododock.webserver.web.v1alpha1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.article.ArticleService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.dto.article.V1alpha1Article;
import ododock.webserver.web.v1alpha1.dto.article.V1alpha1ArticleListOptions;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(ResourcePath.API + ResourcePath.API_VERSION)
@RequiredArgsConstructor
public class V1alpha1ArticleController {

    private final ArticleService articleService;

    @GetMapping(
            value = ResourcePath.ARTICLES + "/{" + ResourcePath.PATH_VAR_ID + "}",
            produces = APPLICATION_JSON_VALUE)
    public Mono<V1alpha1Article> getArticle(
            final @PathVariable String id) {
        return Mono.from(articleService.getArticle(id))
                .map(V1alpha1Article::toControllerDto);
    }

    @GetMapping(
            value = ResourcePath.ACCOUNTS +"/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ARTICLES,
            produces = APPLICATION_JSON_VALUE)
    public Flux<V1alpha1Article> listArticles(
            final @PathVariable Long id,
            final @RequestParam(required = false) V1alpha1ArticleListOptions listOptions) {
        V1alpha1ArticleListOptions safeListOptions =
                (listOptions != null) ? listOptions : V1alpha1ArticleListOptions.builder().build();
        return Flux.from(articleService.listArticles(id, safeListOptions.toDomainDto()))
                .map(V1alpha1Article::toControllerDto);
    }

    @PostMapping(
            value = ResourcePath.ARTICLES,
            produces = APPLICATION_JSON_VALUE)
    public Mono<V1alpha1Article> createArticle(
            final @Valid @RequestBody V1alpha1Article article) {
        return Mono.from(articleService.createArticle(article.toDomainDto()))
                .map(V1alpha1Article::toControllerDto);
    }

    @PatchMapping(
            value = ResourcePath.ARTICLES + "/{" + ResourcePath.PATH_VAR_ID + "}")
    public Mono<V1alpha1Article> updateArticle(
            final @PathVariable String id,
            final @Valid @RequestBody V1alpha1Article request) {
        return Mono.from(articleService.updateArticle(id, request.toDomainDto()))
                .map(V1alpha1Article::toControllerDto);
    }

    @DeleteMapping(
            value = ResourcePath.ARTICLES + "/{" + ResourcePath.PATH_VAR_ID + "}")
    public Mono<Void> deleteArticle(
            final @PathVariable String id) {
        return Mono.from(articleService.deleteArticle(id));
    }

}
