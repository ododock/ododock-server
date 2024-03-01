package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CategoryCreate (
        @NotBlank
        String name,
        @NotBlank
        boolean visibility
) {
}
