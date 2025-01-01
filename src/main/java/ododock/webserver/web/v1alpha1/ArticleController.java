package ododock.webserver.web.v1alpha1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.article.ArticleService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.dto.request.ArticleCreate;
import ododock.webserver.web.v1alpha1.dto.request.ArticleUpdate;
import ododock.webserver.web.v1alpha1.dto.response.ApiResponse;
import ododock.webserver.web.v1alpha1.dto.response.ArticleDetailsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ARTICLES)
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}",
            produces = APPLICATION_JSON_VALUE
    )
    public ArticleDetailsResponse getArticle(
            final @PathVariable Long id
    ) {
        return articleService.getArticle(id);
    }

    @PostMapping(
            value = "",
            produces = APPLICATION_JSON_VALUE
    )
    public ApiResponse createArticle(
            final @Valid @RequestBody ArticleCreate request
    ) {
        Long articleId = articleService.createArticle(request);
        return ApiResponse.of("articleId", articleId);
    }

    @PatchMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}"
    )
    public ResponseEntity<Void> updateArticle(
            final @PathVariable Long id,
            final @Valid @RequestBody ArticleUpdate request
    ) {
        articleService.updateArticle(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}"
    )
    public ResponseEntity<Void> deleteArticle(
            final @PathVariable Long id
    ) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok().build();
    }

}
