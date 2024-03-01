package ododock.webserver.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ododock.webserver.request.ArticleCreate;
import ododock.webserver.request.ArticleUpdate;
import ododock.webserver.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/api/v1/articles")
    public ResponseEntity<Long> createArticle(
            final @Valid @RequestBody ArticleCreate request
    ) {
        Long l = articleService.registerArticle(request);
        return ResponseEntity.ok(l);
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
