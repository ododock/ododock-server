package ododock.webserver.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CompleteDaoAccountVerification(
        @NotBlank
        String email,
        @NotBlank
        String code
) {
}
