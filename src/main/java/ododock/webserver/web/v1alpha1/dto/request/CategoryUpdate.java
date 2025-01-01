package ododock.webserver.web.v1alpha1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CategoryUpdate(
        @NotBlank
        String name,
        @NotNull
        Integer position,
        @NotNull
        boolean visibility
) {
}
