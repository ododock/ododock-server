package ododock.webserver.domain.account;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.security.response.OAuth2UserInfo;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class SocialAccount {

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "email")
    private String email;

    // attributes 추가해야하나?

    @Builder
    public SocialAccount(String provider, String providerId, String email, Account ownerAccount) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
    }

    public SocialAccount updateSocialAccount(final OAuth2UserInfo userInfo) {
        /**
         * nothing to update yet
         */
        return this;
    }

}
