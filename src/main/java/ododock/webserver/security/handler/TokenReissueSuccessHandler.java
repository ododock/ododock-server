package ododock.webserver.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.security.JwtService;
import ododock.webserver.security.response.UserPrincipal;
import ododock.webserver.security.response.V1alpha1Token;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class TokenReissueSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication
    ) throws IOException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        Jwt principal = (Jwt) authenticationToken.getPrincipal();
        String accessToken = jwtService.generateAccessToken(UserPrincipal.from(principal));
        String refreshToken = extractRefreshToken(request);
        if (jwtService.requireRenewRefreshToken(refreshToken)) {
            refreshToken = jwtService.generateRefreshToken(UserPrincipal.from(principal));
        }

        String tokenStr = objectMapper.writeValueAsString(
                V1alpha1Token.builder()
                        .sub(String.valueOf(authenticationToken.getToken().getSubject()))
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );

        final Cookie atCookie = new Cookie("access_token", accessToken);
        atCookie.setPath("/");
        atCookie.setSecure(true);
        atCookie.setAttribute("SameSite", "None");

        final Cookie rtCookie = new Cookie("refresh_token", refreshToken);
        rtCookie.setPath("/");
        rtCookie.setSecure(true);
        rtCookie.setAttribute("SameSite", "None");

        response.addCookie(atCookie);
        response.addCookie(rtCookie);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.getWriter().write(tokenStr);
    }

    private String extractRefreshToken(final HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName()
                        .equals("refresh_token")
                ).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"))
                .getValue();
    }

}
