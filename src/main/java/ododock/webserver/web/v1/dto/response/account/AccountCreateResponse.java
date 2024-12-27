package ododock.webserver.web.v1.dto.response.account;

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
