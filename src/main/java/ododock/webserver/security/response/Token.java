package ododock.webserver.security.response;

import lombok.Builder;

@Builder
public record Token(
        String accessToken,
        String refreshToken
) {
}
