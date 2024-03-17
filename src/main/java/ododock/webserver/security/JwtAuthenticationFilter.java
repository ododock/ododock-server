package ododock.webserver.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ododock.webserver.domain.account.TokenRecord;
import ododock.webserver.repository.TokenRecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static ododock.webserver.security.SecurityConstants.JWT_ACCESS_EXPIRATION;
import static ododock.webserver.security.SecurityConstants.JWT_REFRESH_EXPIRATION;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenRecordRepository tokenRecordRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(
            final TokenRecordRepository tokenRecordRepository,
            final AuthenticationManager authenticationManager,
            final JwtUtil jwtUtil
    ) {
        this.tokenRecordRepository = tokenRecordRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.setFilterProcessesUrl("/api/v1/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
        final String email = obtainUsername(request);
        final String password = obtainPassword(request);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String username = userDetails.getUsername();
        String password = userDetails.getPassword();
        List<String> roles = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        saveToken(response, username, roles);

        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
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
