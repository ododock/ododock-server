package ododock.webserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.Authorization;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.repository.AuthorizationRepository;
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

    private final AuthorizationRepository authorizationRepository;
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
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            log.error("refresh token is null");
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            log.error("invalid refresh token");
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        try {
            String type = jwtUtil.getType(refresh);
            if (!type.equals("refresh")) {
                return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);
            }
            String username = jwtUtil.getUsername(refresh);
            List<String> roles = jwtUtil.getRoles(refresh);

            updateToken(response, username, roles);
            log.info("updated token settings");

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        log.info("reissued access token");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    protected void updateToken(final HttpServletResponse response, final String username, final List<String> roles) throws JsonProcessingException {
        Date issuedAt = new Date();
        Date accessExp = new Date(System.currentTimeMillis() + JWT_ACCESS_EXPIRATION);
        Date refreshExp = new Date(System.currentTimeMillis() + JWT_REFRESH_EXPIRATION);

        String accessToken = jwtUtil.generateToken("accessToken", username, roles, issuedAt, accessExp);
        String refreshToken = jwtUtil.generateToken("refreshToken", username, roles, issuedAt, refreshExp);

        Authorization authorization = authorizationRepository.findByRefreshTokenValue(refreshToken)
                .orElseThrow(); // TODO Non exist refresh token exception
        // TODO 지워야할까?
//        log.info("delete prev token settings");
//        authorizationRepository.deleteByRefreshTokenValue(refreshToken);
        authorization.updateAccessToken(accessToken, jwtUtil.getIssuedTime(accessToken), jwtUtil.getExpiredTime(accessToken));
        authorization.updateRefreshToken(refreshToken, jwtUtil.getIssuedTime(refreshToken), jwtUtil.getExpiredTime(refreshToken));

        response.setHeader("access", accessToken);
        response.addCookie(jwtUtil.createCookie("refresh", refreshToken));
    }

}
