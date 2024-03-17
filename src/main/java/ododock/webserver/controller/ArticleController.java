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
            @PathVariable final Long articleId
    ) {
        return articleService.getArticle(articleId);
    }

    @PostMapping(value = "/api/v1/articles", produces = APPLICATION_JSON_VALUE)
    public ApiResponse createArticle(
            @Valid @RequestBody final ArticleCreate request
    ) {
        Long articleId = articleService.createArticle(request);
        return ApiResponse.of("articleId", articleId);
    }

    @PatchMapping("/api/v1/articles/{articleId}")
    public ResponseEntity<Void> updateArticle(
            @PathVariable final Long articleId,
            @Valid @RequestBody final ArticleUpdate request
    ) {
        articleService.updateArticle(articleId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/articles/{articleId}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable final Long articleId
    ) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.ok().build();
    }

}
