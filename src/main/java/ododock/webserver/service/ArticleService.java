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
import ododock.webserver.response.ArticleDetailsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    final private ArticleRepository articleRepository;
    final private ProfileRepository profileRepository;
    final private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public ArticleDetailsResponse getArticle(final Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException(Article.class, "articleId"));
        Optional<Category> category = Optional.ofNullable(article.getCategory()); // TODO Optional처리 없이 메소드 체이닝 호출 시? 직접 테스트 필요
        return ArticleDetailsResponse.builder()
                .articleId(articleId)
                .title(article.getTitle())
                .body(article.getBody())
                .categoryName(category.map(Category::getName).orElse(null))
                .categoryId(category.map(Category::getId).orElse(null))
                .tags(article.getTags())
                .visibility(article.isVisibility())
                .createdDate(article.getCreatedDate())
                .lastModifiedDate(article.getLastModifiedAt())
                .build();
    }

    @Transactional
    public Long createArticle(ArticleCreate request) {
        profileRepository.findById(request.profileId())
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, request.profileId()));
        Category category = null;
        boolean defaultVisibility = true;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(Category.class, request.categoryId()));
        }
        if (request.visibility() != null) {
            defaultVisibility = category.isVisibility();
        }
        Article newArticle = Article.builder()
                .title(request.title())
                .body(request.body())
                .tags(request.tags())
                .category(category)
                .visibility(defaultVisibility)
                .build();
        return articleRepository.save(newArticle).getId();
    }

    @Transactional
    public Long updateArticle(Long articleId, ArticleUpdate request) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException(Article.class, String.valueOf(articleId)));
        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(Category.class, String.valueOf(request.categoryId())));
        }
        boolean defaultVisibility = category.isVisibility();
        if (request.visibility() != null) {
            defaultVisibility = request.visibility();
        }
        article.updateTitle(request.title());
        article.updateBody(request.body());
        article.updateCategory(category);
        article.updateTags(request.tags());
        article.updateVisibility(defaultVisibility);
        return article.getId();
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException());
        articleRepository.delete(article);
    }

}
