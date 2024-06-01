package ododock.webserver.request;

import lombok.Builder;

@Builder
public record AccountUpdate(
        String nickname,
        String fullname,
        String password
) {
}
