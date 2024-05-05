package ododock.webserver.domain.account;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.security.response.OAuth2UserInfo;

@Entity
@Getter
@Table(name = "social_accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_account_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Account daoAccount;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "email")
    private String email;

    // attributes 추가해야하나?

    @Builder
    public SocialAccount(
            final String provider,
            final String providerId,
            final String email,
            final Account daoAccount
    ) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.daoAccount = daoAccount;
    }

    public SocialAccount updateSocialAccount(final OAuth2UserInfo userInfo) {
        this.provider = userInfo.getProvider();
        this.email = userInfo.getEmail();
        this.providerId = userInfo.getProviderId();
        return this;
    }

}
