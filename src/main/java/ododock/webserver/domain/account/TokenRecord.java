package ododock.webserver.domain.account;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Entity
@Table(name = "token_record")
public class TokenRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_record_id")
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

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

    @Nullable
    @Column(name = "token_status")
    @Enumerated(EnumType.STRING)
    private TokenStatus tokenStatus;

    public TokenRecord setAccessToken(final JwtClaimsSet claimsSet) {
        this.accessTokenIssuedAt = LocalDateTime.ofInstant(claimsSet.getIssuedAt(), ZoneId.systemDefault());
        this.accessTokenExpiresAt = LocalDateTime.ofInstant(claimsSet.getExpiresAt(), ZoneId.systemDefault());
        return this;
    }

    public TokenRecord setRefreshToken(final JwtClaimsSet claimsSet) {
        this.refreshTokenIssuedAt = LocalDateTime.ofInstant(claimsSet.getIssuedAt(), ZoneId.systemDefault());
        this.refreshTokenExpiresAt = LocalDateTime.ofInstant(claimsSet.getExpiresAt(), ZoneId.systemDefault());
        return this;
    }

}
