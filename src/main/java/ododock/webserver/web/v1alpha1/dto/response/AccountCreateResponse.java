package ododock.webserver.web.v1alpha1.dto.response;

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
