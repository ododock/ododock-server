package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CategoryUpdate(
        @NotBlank
        String name,
        @NotNull
        Integer order,
        @NotNull
        boolean visibility
) {
}
