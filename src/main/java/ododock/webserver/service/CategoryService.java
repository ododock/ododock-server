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
import ododock.webserver.request.CategoryOrderUpdate;
import ododock.webserver.request.CategoryUpdate;
import ododock.webserver.response.CategoryDetailsResponse;
import ododock.webserver.response.ListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
                ownerProfile.getCategorySize(),
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
    public List<Category> updateCategoryOrder(final Long profileId, final Long targetCategoryId, final CategoryOrderUpdate request) {
        Profile foundProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));
        Category foundCategory = categoryRepository.findById(targetCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, targetCategoryId));
        Category targetCategory = categoryRepository.findByOwnerProfileAndOrder(foundProfile, request.newOrder())
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, "order", request.newOrder().longValue()));
        targetCategory.updateOrder(foundCategory.getOrder());
        foundCategory.updateOrder(request.newOrder());
        return foundProfile.getCategories();
    }

    @Transactional
    public List<Category> updateCategory(final Long profileId, final Long categoryId, final CategoryUpdate request) {
        Profile foundProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
        if (!category.getOrder().equals(request.order())) {
            Optional<Category> targetCategory = categoryRepository.findByOwnerProfileAndOrder(foundProfile, request.order());
            if (targetCategory.isEmpty()) {
                if (foundProfile.getCategorySize() < request.order()) {
                    throw new IllegalArgumentException("requested category order exceed existing categories range");
                }
            }
            targetCategory.get().updateOrder(category.getOrder());
            category.updateOrder(request.order());
        }
        category.updateName(request.name());
        category.updateVisibility(request.visibility());
        return foundProfile.getCategories();
    }

    @Transactional
    public void deleteCategory(final Long profileId, final Long categoryId) {
        Profile foundProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));
        Category foundCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
        foundProfile.deleteCategory(foundCategory);
        List<Article> articles = articleRepository.findArticleByCategory(foundCategory);
        for (Article article : articles) {
            article.updateCategory(null);
        }
        categoryRepository.deleteById(categoryId);
    }

}
