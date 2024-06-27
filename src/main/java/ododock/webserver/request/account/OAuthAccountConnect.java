package ododock.webserver.request.account;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * OAuth2 Account Connection request DTO class.
 * @param oauthProvider represents OAuth2 Provider i.e. "google", "naver"
 * @param targetAccountId represents subject account which will be merged
 */
@Builder
public record OAuthAccountConnect(
        @NotNull
        String oauthProvider,
        @NotNull
        Long targetAccountId
) {

}
