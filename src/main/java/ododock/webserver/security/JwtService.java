package ododock.webserver.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.repository.jpa.TokenRecordRepository;
import ododock.webserver.security.config.JwtProperties;
import ododock.webserver.security.response.UserPrincipal;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class JwtService {

    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final TokenRecordRepository tokenRecordRepository;

    public TokenRecord generateToken(final UserPrincipal userPrincipal) {
        final TokenRecord tokenRecord = new TokenRecord();
        tokenRecord.setAccountId(userPrincipal.id());

        final Instant now = Instant.now();
        final JwtClaimsSet accessTokenClaim = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.getAccessTokenExpiry(), ChronoUnit.valueOf(jwtProperties.getTokenExpiryTimeUnit().toUpperCase())))
                .subject(userPrincipal.id())
                .claim("provider", userPrincipal.provider())
                .build();
        final JwtClaimsSet refreshTokenClaim = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.getRefreshTokenExpiry(), ChronoUnit.valueOf(jwtProperties.getTokenExpiryTimeUnit().toUpperCase())))
                .subject(userPrincipal.id())
                .id(userPrincipal.id())
                .build();
        tokenRecord.setAccessToken(accessTokenClaim);
        tokenRecord.setAccessTokenValue(jwtEncoder.encode(JwtEncoderParameters.from(accessTokenClaim)).getTokenValue());
        tokenRecord.setRefreshToken(refreshTokenClaim);
        tokenRecord.setRefreshTokenValue(jwtEncoder.encode(JwtEncoderParameters.from(refreshTokenClaim)).getTokenValue());
        tokenRecord.setTokenStatus(TokenStatus.ACTIVE);

        return tokenRecordRepository.save(tokenRecord);
    }

    public String generateAccessToken(UserPrincipal userPrincipal) {
        final JwtClaimsSet accessTokenClaim = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(jwtProperties.getAccessTokenExpiry(), ChronoUnit.MINUTES))
                .subject(userPrincipal.id())
                .claim("provider", userPrincipal.provider())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(accessTokenClaim)).getTokenValue();
    }

    public String generateRefreshToken(UserPrincipal userPrincipal) {
        final JwtClaimsSet refreshTokenClaim = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(jwtProperties.getRefreshTokenExpiry(), ChronoUnit.MINUTES))
                .subject(userPrincipal.id())
                .id(userPrincipal.id())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(refreshTokenClaim)).getTokenValue();
    }

    public boolean requireRenewRefreshToken(String refreshTokenValue) {
        Jwt refreshToken = null;
        try {
            refreshToken = this.jwtDecoder.decode(refreshTokenValue);
        } catch (JwtException ex) {
            log.info("Invalid JWT: {}", ex.getMessage());
        }

        if (refreshToken != null) {
            Instant expireAt = refreshToken.getClaimAsInstant(JwtClaimNames.EXP);
            Long renewThreshold = getThreshold();
            ChronoUnit timeUnit = ChronoUnit.valueOf(jwtProperties.getTokenExpiryTimeUnit().toUpperCase());

            return expireAt.minus(renewThreshold, timeUnit).isBefore(Instant.now());
        }

        return false;
    }

    private Long getThreshold() {
        return jwtProperties.getRefreshTokenEarlyRenewalPeriod() == null
                ? jwtProperties.getRefreshTokenExpiry() / 2
                : jwtProperties.getRefreshTokenEarlyRenewalPeriod();
    }

}
