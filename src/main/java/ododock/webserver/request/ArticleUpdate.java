package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.util.Set;

@Builder
public record ArticleUpdate(
        @NotBlank
        String title,

        @NotNull
        String body,

        @NotNull
        Set<String> tags,

        @Nullable
        Long categoryId
) {
}
