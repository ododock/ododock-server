package ododock.webserver.security.service;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.account.TokenRecord;
import ododock.webserver.domain.account.TokenStatus;
import ododock.webserver.repository.TokenRecordRepository;
import ododock.webserver.security.DaoUserDetails;
import ododock.webserver.security.config.JwtProperties;
import ododock.webserver.security.util.OAuth2UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;
    private final TokenRecordRepository tokenRecordRepository;

    public TokenRecord generateToken(final Authentication authentication) {
        final TokenRecord tokenRecord = new TokenRecord();
        generateAccessToken(authentication, tokenRecord);
        generateRefreshToken(authentication, tokenRecord);
        return tokenRecordRepository.save(tokenRecord);
    }

    public TokenRecord generateToken(final OAuth2AuthenticationToken authentication) {
        final TokenRecord tokenRecord = new TokenRecord();
        generateAccessToken(authentication, tokenRecord);
        generateRefreshToken(authentication, tokenRecord);
        return tokenRecordRepository.save(tokenRecord);
    }

    private void generateAccessToken(final Authentication authentication, final TokenRecord tokenRecord) {
        final Instant now = Instant.now();
        final DaoUserDetails userDetails = (DaoUserDetails) authentication.getPrincipal();
        final String sub = String.valueOf(userDetails.getAccountId());
        tokenRecord.setAccountId(sub);

        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.getAccessTokenExpiry(), ChronoUnit.MINUTES))
                .subject(sub) // username
                .claim("roles", authentication.getAuthorities().stream().map(Object::toString).toList())
                .claim("provider", "ododock")
                .build();
        tokenRecord.setAccessToken(claims);
        tokenRecord.setTokenStatus(TokenStatus.ACTIVE);
        tokenRecord.setAccessTokenValue(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
    }

    private void generateAccessToken(final OAuth2AuthenticationToken authentication, final TokenRecord tokenRecord) {
        final Instant now = Instant.now();
        final Map<String, String> attributes = OAuth2UserMapper.resolveAttributes(authentication);

        final List<String> roles = authentication.getAuthorities().stream().map(Object::toString).toList();
        tokenRecord.setAccountId(attributes.get("accountId"));
        tokenRecord.setProvider(authentication.getAuthorizedClientRegistrationId());

        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.getAccessTokenExpiry(), ChronoUnit.MINUTES))
                .subject(tokenRecord.getAccountId()) // username
                .claim("roles", roles)
                .claim("provider", tokenRecord.getProvider())
                .build();
        tokenRecord.setAccessToken(claims);
        tokenRecord.setTokenStatus(TokenStatus.ACTIVE);
        tokenRecord.setAccessTokenValue(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
    }

    private void generateRefreshToken(final Authentication authentication, final TokenRecord tokenRecord) {
        final Instant now = Instant.now();
        // TODO 리프레시 토큰 정보 수정
        // 토큰id, 사용자id, 클라이언트id, scope
        final JwtClaimsSet claims = JwtClaimsSet.builder()
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
