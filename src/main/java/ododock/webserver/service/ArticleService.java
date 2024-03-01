package ododock.webserver.service;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.article.Article;
import ododock.webserver.domain.profile.Category;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.exception.ResourceNotFoundException;
import ododock.webserver.repository.ArticleRepository;
import ododock.webserver.repository.CategoryRepository;
import ododock.webserver.repository.ProfileRepository;
import ododock.webserver.request.ArticleCreate;
import ododock.webserver.request.ArticleUpdate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    final private ArticleRepository articleRepository;
    final private ProfileRepository profileRepository;
    final private CategoryRepository categoryRepository;

    @Transactional
    public Long registerArticle(ArticleCreate request) {
        profileRepository.findById(request.profileId())
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, request.profileId()));
        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(Category.class, request.categoryId()));
        }

        Article newArticle = Article.builder()
                .title(request.title())
                .body(request.body())
                .tags(request.tags())
                .category(category)
                .build();
        return articleRepository.save(newArticle).getId();
    }

    @Transactional
    public Long updateArticle(Long articleId, ArticleUpdate request) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException(Article.class, String.valueOf(articleId)));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, String.valueOf(request.categoryId())));
        article.updateTitle(request.title());
        article.updateBody(request.body());
        article.updateCategory(category);
        article.updateTags(request.tags());
        return article.getId();
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException());
        articleRepository.delete(article);
    }

}
