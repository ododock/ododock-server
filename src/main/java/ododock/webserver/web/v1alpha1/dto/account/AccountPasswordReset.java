package ododock.webserver.web.v1alpha1.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountPasswordReset(
        @NotBlank
        String verificationCode,
        @NotBlank
        String newPassword
) {
}
