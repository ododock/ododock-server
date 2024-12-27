package ododock.webserver.web.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.article.Category;
import ododock.webserver.domain.article.CategoryService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1.dto.CategoryCreate;
import ododock.webserver.web.v1.dto.CategoryPositionUpdate;
import ododock.webserver.web.v1.dto.CategoryUpdate;
import ododock.webserver.web.v1.dto.response.ApiResponse;
import ododock.webserver.web.v1.dto.response.CategoryDetailsResponse;
import ododock.webserver.web.v1.dto.response.ListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_CATEGORIES)
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(
            value = ""
    )
    public ListResponse<CategoryDetailsResponse> getCategoriesByAccountId(
            final @PathVariable Long id
    ) {
        return categoryService.getCategoriesByAccountId(id);
    }

    @PostMapping(
            value = ""
    )
    public ApiResponse createCategory(
            final @PathVariable Long id,
            final @Valid @RequestBody CategoryCreate request
    ) {
        return ApiResponse.of("categoryId", categoryService.createCategory(id, request));
    }

    @PatchMapping(
            value = "/{" + ResourcePath.PATH_VAR_SUB_ID + "}"
    )
    public ListResponse<CategoryDetailsResponse> updateCategory(
            final @PathVariable Long id,
            final @PathVariable Long subId,
            final @Valid @RequestBody CategoryUpdate request
    ) {
        final List<Category> categories = categoryService.updateCategory(id, subId, request);
        return ListResponse.of(
                id,
                categories.size(),
                categories.stream().map(CategoryDetailsResponse::of).collect(Collectors.toList())
        );
    }

    @PatchMapping(
            value = "/{" + ResourcePath.PATH_VAR_SUB_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_CATEGORIES_POSITION
    )
    public ListResponse<CategoryDetailsResponse> updateCategoryPosition(
            final @PathVariable Long id,
            final @PathVariable Long subId,
            final @Valid @RequestBody CategoryPositionUpdate request
    ) {
        final List<Category> updateCategories = categoryService.updateCategoryPosition(id, subId, request);
        return ListResponse.of(
                id,
                updateCategories.size(),
                updateCategories.stream().map(CategoryDetailsResponse::of).collect(Collectors.toList())
        );
    }

    @DeleteMapping(
            value = "/{"+ResourcePath.PATH_VAR_SUB_ID + "}"
    )
    public ResponseEntity<Void> deleteCategory(
            final @PathVariable Long id,
            final @PathVariable Long subId
    ) {
        categoryService.deleteCategory(id, subId);
        return ResponseEntity.ok().build();
    }

}
