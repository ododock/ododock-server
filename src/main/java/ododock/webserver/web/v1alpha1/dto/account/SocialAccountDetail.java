package ododock.webserver.web.v1alpha1.dto.account;

import lombok.Builder;
import ododock.webserver.domain.account.SocialAccount;
import org.springframework.lang.Nullable;

@Builder
public record SocialAccountDetail(
        String provider,
        String providerId,
        @Nullable
        String email
) {
    public static SocialAccountDetail of(final SocialAccount socialAccount) {
        return SocialAccountDetail.builder()
                .provider(socialAccount.getProvider())
                .providerId(socialAccount.getProviderId())
                .email(socialAccount.getEmail())
                .build();
    }
}
