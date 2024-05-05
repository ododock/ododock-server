package ododock.webserver.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OAuthAccountConnect(
        @NotNull
        String oauthProvider,
        @NotNull
        Long targetAccountId
) {

}
