package ododock.webserver.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final ObjectMapper objectMapper;
    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret") final String secret) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
        this.objectMapper = new ObjectMapper();
    }

    public String getUsername(final String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().get("username", String.class);
    }

    public List<String> getRoles(final String token) throws JsonProcessingException {
        String roles = Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().get("roles", String.class);
        return objectMapper.readValue(roles, List.class);
    }

    public boolean isExpired(final String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().getExpiration().before(new Date());
    }

    public JwtToken generateToken(final String username, List<String> roles) throws JsonProcessingException {
        String accessToken = Jwts.builder()
                .claim("username", username)
                .claim("roles", objectMapper.writeValueAsString(roles))
                .issuedAt(new Date())
                // 보통 30분 ~ 1시간
                .expiration(new Date(System.currentTimeMillis() + SecurityConstants.JWT_ACCESS_EXPIRATION))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .claim("username", username)
                .claim("roles", roles)
                .issuedAt(new Date())
                // 보통 30분 ~ 1시간
                .expiration(new Date(System.currentTimeMillis() + SecurityConstants.JWT_REFRESH_EXPIRATION))
                .signWith(secretKey)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


}
