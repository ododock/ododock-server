package ododock.webserver.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final ObjectMapper objectMapper;
    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}") final String secret) {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
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

    public String getType(final String token) throws JsonProcessingException {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("type", String.class);
    }

    public LocalDateTime getIssuedTime(final String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload()
                .getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public LocalDateTime getExpiredTime(final String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload()
                .getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public boolean isExpired(final String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().getExpiration().before(new Date());
    }

    public String generateToken(
            final String type,
            final String username,
            final List<String> roles,
            final Date issuedAt,
            final Date expiredAt) throws JsonProcessingException {
        return Jwts.builder()
                .claim("username", username)
                .claim("roles", objectMapper.writeValueAsString(roles))
                .claim("type", type)
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // TODO jwt관련 util클래스에 cookie 생성 메소드를 두는게 맞나?
    public Cookie createCookie(final String type, final String token) {
        Cookie cookie = new Cookie(type, token);
        cookie.setMaxAge(24*60*60);
//        cookie.setSecure(true); // https 통신인경우
//        cookie.setPath("/"); // 쿠키가 적용될 범위
        cookie.setHttpOnly(true);
        return cookie;
    }

}
