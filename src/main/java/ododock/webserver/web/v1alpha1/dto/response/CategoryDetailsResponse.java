package ododock.webserver.web.v1alpha1.dto.response;

import lombok.Builder;
import ododock.webserver.domain.article.Category;

@Builder
public record CategoryDetailsResponse(
        Long categoryId,
        String name,
        Integer position,
        boolean visibility
) {
    public static CategoryDetailsResponse of(Category category) {
        return CategoryDetailsResponse.builder()
                .name(category.getName())
                .position(category.getPosition())
                .visibility(category.isVisibility())
                .build();
    }
}
