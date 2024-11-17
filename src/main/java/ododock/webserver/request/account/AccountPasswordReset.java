package ododock.webserver.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountPasswordReset(
        @NotBlank
        String email,
        @NotBlank
        String verificationCode,
        @NotBlank
        String newPassword
) {
}
