package ododock.webserver.response.account;

import lombok.Builder;
import ododock.webserver.domain.account.VerificationInfo;

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
