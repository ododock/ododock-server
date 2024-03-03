package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountPasswordUpdate(
        @NotBlank
        String password
) {
}
