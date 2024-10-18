package ododock.webserver.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountPasswordReset(
        @NotBlank
        String code,
        @NotBlank
        String newPassword
) {
}
