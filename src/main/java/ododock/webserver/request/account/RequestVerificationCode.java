package ododock.webserver.request.account;

import lombok.Builder;

@Builder
public record RequestVerificationCode(
        Long accountId,
        String email
) {
}
