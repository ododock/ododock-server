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

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final ProfileRepository profileRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public ListResponse<CategoryDetailsResponse> getCategoryByProfileId(final Long profileId) {
        Profile ownerProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));
        List<Category> categories = categoryRepository.findByOwnerProfile(ownerProfile);
        return ListResponse.of(
                categories.stream()
                        .map(CategoryDetailsResponse::of)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void createCategory(final Long profileId, final CategoryCreate request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));
        Category category = Category.builder()
                .ownerProfile(profile)
                .name(request.name())
                .visibility(request.visibility())
                .build();
        categoryRepository.save(category);
    }

    @Transactional
    public void updateCategoryList(final Long profileId, final CategoryListUpdate request) {
        profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));

        for (CategoryListUpdate.CategoryDto targetCategory : request.categories()) {
            Category foundCategory = categoryRepository.findById(targetCategory.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(Category.class, targetCategory.getCategoryId()));
            foundCategory.updateName(targetCategory.getName());
            foundCategory.updateVisibility(targetCategory.isVisibility());
        }
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
