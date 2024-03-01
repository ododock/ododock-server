package ododock.webserver.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ArticleDetailsResponse(
        String title,
        String category,
        String body,
        Set<String> tags,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
