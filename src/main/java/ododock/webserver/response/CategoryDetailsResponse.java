package ododock.webserver.response;

import lombok.Builder;
import ododock.webserver.domain.profile.Category;

@Builder
public record CategoryDetailsResponse(
        Long categoryId,
        String name,
        Integer order,
        boolean visibility
) {
    public static CategoryDetailsResponse of(Category category) {
        return CategoryDetailsResponse.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .order(category.getOrder())
                .visibility(category.isVisibility())
                .build();
    }
}
