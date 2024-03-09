package ododock.webserver.response;

import lombok.Builder;

@Builder
public record AccountCreateResponse(
        Long accountId,
        Long profileId
) {
}
