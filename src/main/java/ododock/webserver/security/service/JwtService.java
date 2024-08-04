package ododock.webserver.security.service;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.account.TokenRecord;
import ododock.webserver.domain.account.TokenStatus;
import ododock.webserver.repository.TokenRecordRepository;
import ododock.webserver.security.config.JwtProperties;
import ododock.webserver.security.response.UserPrincipal;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class JwtService {

    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;
    private final TokenRecordRepository tokenRecordRepository;

    public TokenRecord generateToken(final UserPrincipal userPrincipal) {

        final TokenRecord tokenRecord = new TokenRecord();
        tokenRecord.setAccountId(userPrincipal.id());

        final Instant now = Instant.now();
        final JwtClaimsSet accessTokenClaim = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.getAccessTokenExpiry(), ChronoUnit.MINUTES))
                .subject(userPrincipal.id())
                .claim("provider", userPrincipal.provider())
                .build();
        final JwtClaimsSet refreshTokenClaim = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.getRefreshTokenExpiry(), ChronoUnit.MINUTES))
                .subject(userPrincipal.id())
                .id(userPrincipal.id())
                .build();
        tokenRecord.setAccessToken(accessTokenClaim);
        tokenRecord.setTokenStatus(TokenStatus.ACTIVE);
        tokenRecord.setAccessTokenValue(jwtEncoder.encode(JwtEncoderParameters.from(accessTokenClaim)).getTokenValue());
        tokenRecord.setRefreshToken(refreshTokenClaim);
        tokenRecord.setRefreshTokenValue(jwtEncoder.encode(JwtEncoderParameters.from(refreshTokenClaim)).getTokenValue());

        return tokenRecordRepository.save(tokenRecord);

    }

}
