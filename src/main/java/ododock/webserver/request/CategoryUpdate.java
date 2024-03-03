package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CategoryUpdate(
        @NotBlank
        Long categoryId,
        @NotBlank
        String name,
        @NotBlank
        boolean visibility
) {
}
