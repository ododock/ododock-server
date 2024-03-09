package ododock.webserver.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ododock.webserver.request.CategoryCreate;
import ododock.webserver.request.CategoryListUpdate;
import ododock.webserver.request.CategoryUpdate;
import ododock.webserver.response.ApiResponse;
import ododock.webserver.response.CategoryDetailsResponse;
import ododock.webserver.response.ListResponse;
import ododock.webserver.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/api/v1/profiles/{profileId}/categories")
    public ListResponse<CategoryDetailsResponse> getCategoriesByProfileId(
            @PathVariable final Long profileId
    ) {
        return categoryService.getCategoriesByProfileId(profileId);
    }

    @PostMapping("/api/v1/profiles/{profileId}/categories")
    public ApiResponse createCategory(
            @PathVariable final Long profileId,
            @Valid @RequestBody final CategoryCreate request
    ) {
        return ApiResponse.of("categoryId", categoryService.createCategory(profileId, request));
    }

    @PatchMapping("/api/v1/profiles/{profileId}/categories")
    public ResponseEntity<Void> updateCategoryList(
            @PathVariable final Long profileId,
            @Valid @RequestBody final CategoryListUpdate request
    ) {
        categoryService.updateCategoryList(profileId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/v1/profiles/{profileId}/categories/{categoryId}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable final Long profileId,
            @PathVariable final Long categoryId,
            @Valid @RequestBody final CategoryUpdate request
    ) {
        categoryService.updateCategory(profileId, categoryId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/profiles/{profileId}/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable final Long profileId,
            @PathVariable final Long categoryId
    ) {
        categoryService.deleteCategory(profileId, categoryId);
        return ResponseEntity.ok().build();
    }

}
