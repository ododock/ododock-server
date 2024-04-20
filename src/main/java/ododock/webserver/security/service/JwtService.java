package ododock.webserver.security.service;

import lombok.RequiredArgsConstructor;
import ododock.webserver.security.config.JwtProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;

    public String generateAccessToken(final Authentication authentication) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.getAccessTokenExpiry(), ChronoUnit.MINUTES))
                .subject(authentication.getName()) // username
                .claim("roles", authentication.getAuthorities().stream().map(Object::toString).toList())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefreshToken(final Authentication authentication) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.getRefreshTokenExpiry(), ChronoUnit.MINUTES))
                .subject(authentication.getName()) // username
                .id(UUID.randomUUID().toString())
                .build();

        /*
        TODO
         TokenRecord Repository에 저장하는 로직 추가 필요
         1. 어떻게 UUID를 생성하고 엔티티에 넣을것인가?
         2. 업데이트의 경우에 기존의 토큰은 폐기한 이후에 생성하는것으로?

         */


        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


}
