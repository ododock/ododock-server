package ododock.webserver.web.v1alpha1.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CompleteDaoAccountVerification(
        @NotBlank
        String email,
        @NotBlank
        String verificationCode
) {
}
