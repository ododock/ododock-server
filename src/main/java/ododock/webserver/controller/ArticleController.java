package ododock.webserver.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ododock.webserver.request.ArticleCreate;
import ododock.webserver.request.ArticleUpdate;
import ododock.webserver.response.ApiResponse;
import ododock.webserver.response.ArticleDetailsResponse;
import ododock.webserver.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/api/v1/articles/{articleId}")
    public ArticleDetailsResponse getArticle(
            final @PathVariable Long articleId
    ) {
        return articleService.getArticle(articleId);
    }

    @PostMapping(value = "/api/v1/articles", produces = APPLICATION_JSON_VALUE)
    public ApiResponse createArticle(
            final @Valid @RequestBody ArticleCreate request
    ) {
        Long articleId = articleService.createArticle(request);
        System.out.println(articleId);
        return ApiResponse.of("articleId", articleId);
    }

    @PatchMapping("/api/v1/articles/{articleId}")
    public ResponseEntity<Void> updateArticle(
            final @PathVariable Long articleId,
            final @Valid @RequestBody ArticleUpdate request
    ) {
        articleService.updateArticle(articleId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/articles/{articleId}")
    public ResponseEntity<Void> deleteArticle(
            final @PathVariable Long articleId
    ) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.ok().build();
    }

}
