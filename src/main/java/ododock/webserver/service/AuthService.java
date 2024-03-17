package ododock.webserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.TokenRecord;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.repository.TokenRecordRepository;
import ododock.webserver.security.CustomUserDetails;
import ododock.webserver.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static ododock.webserver.security.SecurityConstants.JWT_ACCESS_EXPIRATION;
import static ododock.webserver.security.SecurityConstants.JWT_REFRESH_EXPIRATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final TokenRecordRepository tokenRecordRepository;
    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email " + email + " not found"));
        return new CustomUserDetails(account);
    }

    @Transactional
    public ResponseEntity<?> reissueToken(final HttpServletRequest request, final HttpServletResponse response) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        try {
            String type = jwtUtil.getType(refreshToken);
            if (!type.equals("refreshToken")) {
                return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);
            }
            String username = jwtUtil.getUsername(refreshToken);
            List<String> roles = jwtUtil.getRoles(refreshToken);

            revokeToken(refreshToken);
            saveToken(response, username, roles);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public void revokeToken(final String prevToken) {
        TokenRecord tokenRecord = tokenRecordRepository.findByRefreshTokenValue(prevToken)
                .orElseThrow(); // TODO Non exist refresh token exception
        tokenRecordRepository.deleteByRefreshTokenValue(prevToken);
    }

    @Transactional
    public void revokeAllTokensByEmail(final String email) {
        tokenRecordRepository.deleteAllByUsername(email);
    }

    @Transactional
    protected void saveToken(final HttpServletResponse response, final String username, final List<String> roles) throws JsonProcessingException {
        Date issuedAt = new Date();
        Date accessExp = new Date(System.currentTimeMillis() + JWT_ACCESS_EXPIRATION);
        Date refreshExp = new Date(System.currentTimeMillis() + JWT_REFRESH_EXPIRATION);

        String accessToken = jwtUtil.generateToken("accessToken", username, roles, issuedAt, accessExp);
        String refreshToken = jwtUtil.generateToken("refreshToken", username, roles, issuedAt, refreshExp);

        TokenRecord tokenRecord = TokenRecord.builder()
                .username(username)
                .accessTokenValue(accessToken)
                .accessTokenIssuedAt(jwtUtil.getIssuedTime(accessToken))
                .accessTokenExpiresAt(jwtUtil.getExpiredTime(accessToken))
                .refreshTokenValue(refreshToken)
                .refreshTokenIssuedAt(jwtUtil.getIssuedTime(refreshToken))
                .refreshTokenExpiresAt(jwtUtil.getExpiredTime(refreshToken))
                .build();
        tokenRecordRepository.save(tokenRecord);

        response.setHeader("access", accessToken);
        response.addCookie(jwtUtil.createCookie("refresh", refreshToken));
    }

}
