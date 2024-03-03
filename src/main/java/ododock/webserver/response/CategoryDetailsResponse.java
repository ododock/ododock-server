package ododock.webserver.response;

import lombok.Builder;
import ododock.webserver.domain.profile.Category;

@Builder
public record CategoryDetailsResponse(
        Long categoryId,
        String name,
        boolean visibility
) {
    public static CategoryDetailsResponse of(Category category) {
        return CategoryDetailsResponse.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .visibility(category.isVisibility())
                .build();
    }
}
