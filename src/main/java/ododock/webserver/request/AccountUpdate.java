package ododock.webserver.request;

import lombok.Builder;

@Builder
public record AccountUpdate(
        Long accountId,
        String email,
        String username,
        String fullname
) {
}
