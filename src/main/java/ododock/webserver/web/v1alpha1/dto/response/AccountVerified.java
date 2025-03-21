package ododock.webserver.web.v1alpha1.dto.response;

import lombok.Builder;
import ododock.webserver.domain.verification.VerificationInfo;

import java.time.LocalDateTime;

@Builder
public record AccountVerified(
        String code,
        LocalDateTime expiredAt
) {
    public static AccountVerified of(VerificationInfo verificationInfo) {
        return AccountVerified.builder()
                .code(verificationInfo.getCode())
                .expiredAt(verificationInfo.getExpiryTimestamp())
                .build();
    }
}
