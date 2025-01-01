package ododock.webserver.web.v1alpha1.dto.account;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * OAuth2 Account Merge request DTO class.
 * @param oauthProvider represents OAuth2 Provider i.e. "google", "naver"
 * @param targetAccountId represents subject account which will be merged
 */
@Builder
public record OAuthAccountMerge(
        @NotNull
        String oauthProvider,
        @NotNull
        Long targetAccountId
) {

}
