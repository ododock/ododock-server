package ododock.webserver.web.v1.dto.account;

import lombok.Builder;

@Builder
public record RequestVerificationCode(
        Long accountId,
        String email
) {
}
