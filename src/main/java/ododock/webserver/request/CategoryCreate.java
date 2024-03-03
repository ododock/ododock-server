package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CategoryCreate (
        @NotBlank
        String name,
        @NotBlank
        boolean visibility
) {
}
