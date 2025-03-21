package ododock.webserver.web.v1alpha1.dto.account;

import lombok.Builder;

@Builder
public record V1alpha1Verification(
        String email,
        String verificationCode
) {
}
