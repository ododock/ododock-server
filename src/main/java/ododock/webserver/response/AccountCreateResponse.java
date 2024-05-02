package ododock.webserver.response;

import lombok.Builder;
import lombok.Getter;


@Builder
public record AccountCreateResponse(
        @Getter
        Long accountId,
        @Getter
        Long profileId
) {
}
