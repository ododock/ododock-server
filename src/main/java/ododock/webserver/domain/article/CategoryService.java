package ododock.webserver.domain.article;

import lombok.AllArgsConstructor;
import ododock.webserver.domain.account.Account;
import ododock.webserver.web.exception.ResourceNotFoundException;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.repository.ArticleRepository;
import ododock.webserver.repository.CategoryRepository;
import ododock.webserver.web.v1alpha1.dto.request.CategoryCreate;
import ododock.webserver.web.v1alpha1.dto.request.CategoryPositionUpdate;
import ododock.webserver.web.v1alpha1.dto.request.CategoryUpdate;
import ododock.webserver.web.v1alpha1.dto.response.CategoryDetailsResponse;
import ododock.webserver.web.v1alpha1.dto.response.ListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public ListResponse<CategoryDetailsResponse> getCategoriesByAccountId(final Long accountId) {
        Account ownerAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        List<Category> categories = categoryRepository.findByOwnerAccount(ownerAccount);
        return ListResponse.of(
                accountId,
                ownerAccount.getCategories().size(),
                categories.stream()
                        .map(CategoryDetailsResponse::of)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public Long createCategory(final Long accountId, final CategoryCreate request) {
        Account ownerAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        boolean defaultVisibility = true;
        Category category = Category.builder()
                .ownerAccount(ownerAccount)
                .name(request.name())
                .visibility(defaultVisibility)
                .build();
        return categoryRepository.save(category).getId();
    }

    @Transactional
    public List<Category> updateCategoryPosition(final Long accountId, final Long targetCategoryId, final CategoryPositionUpdate request) {
        Account ownerAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        Category foundCategory = categoryRepository.findById(targetCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, targetCategoryId));
        Category targetCategory = categoryRepository.findByOwnerAccountAndPosition(ownerAccount, request.newPosition()) // 변경될 카테고리
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, "position", request.newPosition().longValue()));
        // todo 순서가 변동이 없거나, 마지막으로 지정될 경우 처리 필요
        final int targetCategoryPosition = targetCategory.getPosition();
        foundCategory.updatePosition(request.newPosition());
        targetCategory.updatePosition(targetCategoryPosition);
        return ownerAccount.getCategories();
    }

    @Transactional
    public List<Category> updateCategory(final Long accountId, final Long categoryId, final CategoryUpdate request) {
        Account ownerAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        Category foundCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
        if (!foundCategory.getPosition().equals(request.position())) {
            // todo 카테고리 변경 로직 private 메소드로 뽑을것
            // targetCategory = 변경하고 싶은 위치에 기 존재하던 카테고리
            Optional<Category> existingCategoryOpt = categoryRepository.findByOwnerAccountAndPosition(ownerAccount, request.position());
            if (existingCategoryOpt.isPresent()) {
                Category existingCategory = existingCategoryOpt.get();
                final int positionToMove = existingCategory.getPosition();
                existingCategory.updatePosition(foundCategory.getPosition());
                foundCategory.updatePosition(positionToMove);
            } else {
                if (ownerAccount.getCategories().size() < request.position()) {
                    final int positionToMove = ownerAccount.getCategories().size() - 1;
                    ownerAccount.getCategories().stream()
                            .filter(category -> category.getPosition() > foundCategory.getPosition())
                            .forEach(category -> category.updatePosition(category.getPosition() - 1));
                    foundCategory.updatePosition(positionToMove);
                }
            }
        }
        foundCategory.updateName(request.name());
        foundCategory.updateVisibility(request.visibility());
        return ownerAccount.getCategories();
    }

    @Transactional
    public void deleteCategory(final Long accountId, final Long categoryId) {
        Account ownerAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        Category foundCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
        ownerAccount.deleteCategory(foundCategory);
        // todo domain Account 또는 Category 에서 해결하는것으로 수정 필요
        List<Article> articles = articleRepository.findArticleByCategory(foundCategory);
        for (Article article : articles) {
            article.updateCategory(null);
        }
        categoryRepository.deleteById(categoryId);
    }

}
