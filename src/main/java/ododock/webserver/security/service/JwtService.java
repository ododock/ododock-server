package ododock.webserver.security.service;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.account.TokenRecord;
import ododock.webserver.domain.account.TokenStatus;
import ododock.webserver.repository.TokenRecordRepository;
import ododock.webserver.security.config.JwtProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;
    private final TokenRecordRepository tokenRecordRepository;

    public TokenRecord generateToken(final Authentication authentication) {
        TokenRecord tokenRecord = new TokenRecord();
        generateAccessToken(authentication, tokenRecord);
        generateRefreshToken(authentication, tokenRecord);
        return tokenRecordRepository.save(tokenRecord);
    }

    private void generateAccessToken(final Authentication authentication, final TokenRecord tokenRecord) {
        Instant now = Instant.now();
        DaoUserDetails userDetails = (DaoUserDetails) authentication.getPrincipal();
        tokenRecord.setAccountId(userDetails.getId());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.getAccessTokenExpiry(), ChronoUnit.MINUTES))
                .subject(authentication.getName()) // username
                .claim("roles", authentication.getAuthorities().stream().map(Object::toString).toList())
                .claim("accId", userDetails.getId())
                .build();
        tokenRecord.setAccessToken(claims);
        tokenRecord.setTokenStatus(TokenStatus.ACTIVE);
        tokenRecord.setAccessTokenValue(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
    }

    private void generateRefreshToken(final Authentication authentication, final TokenRecord tokenRecord) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.getRefreshTokenExpiry(), ChronoUnit.MINUTES))
                .subject(authentication.getName()) // username
                .id(UUID.randomUUID().toString())
                .build();
        tokenRecord.setRefreshToken(claims);
        tokenRecord.setRefreshTokenValue(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
    }


}
