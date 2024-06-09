package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.util.Set;

@Builder
public record ArticleCreate(
        @NotNull
        Long profileId,
        @NotBlank
        String title,
        @NotNull
        String body,
        @Nullable
        Set<String> tags,
        @Nullable
        Long categoryId,
        @Nullable
        Boolean visibility
) {
}
