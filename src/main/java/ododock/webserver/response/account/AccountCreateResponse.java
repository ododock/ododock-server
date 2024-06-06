package ododock.webserver.response.account;

import lombok.Builder;
import lombok.Getter;


@Builder
public record AccountCreateResponse(
        @Getter
        Long sub,
        @Getter
        Long profileId
) {
}
