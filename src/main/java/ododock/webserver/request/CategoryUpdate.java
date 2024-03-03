package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.lang.Nullable;

@Builder
public record CategoryUpdate(
        @Nullable // TODO List update에서도 사용됨. 리팩토링 고려
        Long categoryId,
        @NotBlank
        String name,
        @NotNull
        boolean visibility
) {
}
