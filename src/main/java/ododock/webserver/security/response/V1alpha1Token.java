package ododock.webserver.security.response;

import lombok.Builder;

@Builder
public record V1alpha1Token(
        String sub,
        String accessToken,
        String refreshToken
) {
}
