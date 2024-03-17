package ododock.webserver.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ododock.webserver.domain.account.Authorization;
import ododock.webserver.repository.AuthorizationRepository;
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
import java.util.Optional;

import static ododock.webserver.security.SecurityConstants.JWT_ACCESS_EXPIRATION;
import static ododock.webserver.security.SecurityConstants.JWT_REFRESH_EXPIRATION;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthorizationRepository authorizationRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(
            final AuthorizationRepository authorizationRepository,
            final AuthenticationManager authenticationManager,
            final JwtUtil jwtUtil
    ) {
        this.authorizationRepository = authorizationRepository;
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

        // TODO 유저가 email을 변경하게되면 고아row가 되는것아닌가? 그럼 Authentication과정에서 account_id를 넣어야하나? 토큰에 id가 포함되어버리는데?
        Optional<Authorization> opt = authorizationRepository.findByUsername(username);
        if (opt.isPresent()) {
            opt.get().updateAccessToken(accessToken, jwtUtil.getIssuedTime(accessToken), jwtUtil.getExpiredTime(accessToken));
            opt.get().updateRefreshToken(refreshToken, jwtUtil.getIssuedTime(refreshToken), jwtUtil.getExpiredTime(refreshToken));
        } else {
            Authorization authorization = Authorization.builder()
                    .username(username)
                    .accessTokenValue(accessToken)
                    .accessTokenIssuedAt(jwtUtil.getIssuedTime(accessToken))
                    .accessTokenExpiresAt(jwtUtil.getExpiredTime(accessToken))
                    .refreshTokenValue(refreshToken)
                    .refreshTokenIssuedAt(jwtUtil.getIssuedTime(refreshToken))
                    .refreshTokenExpiresAt(jwtUtil.getExpiredTime(refreshToken))
                    .build();
            authorizationRepository.save(authorization);
        }
        response.setHeader("access", accessToken);
        response.addCookie(jwtUtil.createCookie("refresh", refreshToken));
    }
}
