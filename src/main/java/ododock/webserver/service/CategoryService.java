package ododock.webserver.service;

import lombok.AllArgsConstructor;
import ododock.webserver.domain.article.Article;
import ododock.webserver.domain.profile.Category;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.exception.ResourceNotFoundException;
import ododock.webserver.repository.ArticleRepository;
import ododock.webserver.repository.CategoryRepository;
import ododock.webserver.repository.ProfileRepository;
import ododock.webserver.request.CategoryCreate;
import ododock.webserver.request.CategoryListUpdate;
import ododock.webserver.request.CategoryUpdate;
import ododock.webserver.response.CategoryDetailsResponse;
import ododock.webserver.response.ListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final ProfileRepository profileRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public ListResponse<CategoryDetailsResponse> getCategoriesByProfileId(final Long profileId) {
        Profile ownerProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));
        List<Category> categories = categoryRepository.findByOwnerProfile(ownerProfile);
        return ListResponse.of(
                profileId,
                categories.stream()
                        .map(CategoryDetailsResponse::of)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public Long createCategory(final Long profileId, final CategoryCreate request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));
        boolean defaultVisibility = true;
        if (request.visibility() == null) {
            defaultVisibility = request.visibility();
        }
        Category category = Category.builder()
                .ownerProfile(profile)
                .name(request.name())
                .visibility(defaultVisibility)
                .build();
        return categoryRepository.save(category).getId();
    }

    @Transactional
    public void updateCategoryList(final Long profileId, final CategoryListUpdate request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));
        List<Category> categories = new ArrayList<>();
        for (CategoryUpdate singleCategory : request.categories()) {
            Category foundCategory = categoryRepository.findById(singleCategory.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(Category.class, singleCategory.categoryId()));
            categories.add(foundCategory);
            foundCategory.updateName(singleCategory.name());
            foundCategory.updateVisibility(singleCategory.visibility());
        }
        profile.updateCategories(categories);
    }

    @Transactional
    public void updateCategory(final Long profileId, final Long categoryId, final CategoryUpdate request) {
        profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
        category.updateName(request.name());
        category.updateVisibility(request.visibility());
    }

    @Transactional
    public void deleteCategory(final Long profileId, final Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
        List<Article> articles = articleRepository.findArticleByCategory(category);
        for(Article article : articles) {
            article.updateCategory(null);
        }
        categoryRepository.deleteById(categoryId);
    }

}
