package ododock.webserver.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ArticleDetailsResponse(
        Long articleId,
        String title,
        String body,
        Long categoryId,
        String categoryName,
        Set<String> tags,
        boolean visibility,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
