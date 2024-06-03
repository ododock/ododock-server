package ododock.webserver.security.response;

import lombok.Builder;

@Builder
public record Token(
        String sub,
        String accessToken,
        String refreshToken
) {
}
