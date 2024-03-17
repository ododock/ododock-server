package ododock.webserver.domain.account;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "token_record"
)
public class TokenRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_record_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Nullable
    @Column(name = "access_token_value", length = 1000)
    private String accessTokenValue;

    @Nullable
    private LocalDateTime accessTokenIssuedAt;

    @Nullable
    private LocalDateTime accessTokenExpiresAt;

    @Nullable
    @Column(name = "refresh_token_value", length = 1000)
    private String refreshTokenValue;

    @Nullable
    private LocalDateTime refreshTokenIssuedAt;

    @Nullable
    private LocalDateTime refreshTokenExpiresAt;

    @Builder
    public TokenRecord(
            final String username,
            @Nullable final String accessTokenValue,
            @Nullable final LocalDateTime accessTokenIssuedAt,
            @Nullable final LocalDateTime accessTokenExpiresAt,
            @Nullable final String refreshTokenValue,
            @Nullable final LocalDateTime refreshTokenIssuedAt,
            @Nullable final LocalDateTime refreshTokenExpiresAt) {
        this.username = username;
        this.accessTokenValue = accessTokenValue;
        this.accessTokenIssuedAt = accessTokenIssuedAt;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.refreshTokenValue = refreshTokenValue;
        this.refreshTokenIssuedAt = refreshTokenIssuedAt;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }

    public void updateAccessToken(final String accessToken, final LocalDateTime issuedAt, final LocalDateTime expiresAt) {
        this.accessTokenValue = accessToken;
        this.accessTokenIssuedAt = issuedAt;
        this.accessTokenExpiresAt = expiresAt;
    }

    public void updateRefreshToken(final String refreshToken, final LocalDateTime issuedAt, final LocalDateTime expiresAt) {
        this.refreshTokenValue = refreshToken;
        this.refreshTokenIssuedAt = issuedAt;
        this.refreshTokenExpiresAt = expiresAt;
    }

}
