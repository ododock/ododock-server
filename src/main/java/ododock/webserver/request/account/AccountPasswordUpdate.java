package ododock.webserver.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountPasswordUpdate(
        @NotBlank
        String password
) {
}
