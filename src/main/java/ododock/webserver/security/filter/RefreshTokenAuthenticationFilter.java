package ododock.webserver.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.security.JwtService;
import ododock.webserver.security.handler.TokenReissueSuccessHandler;
import ododock.webserver.web.ResourcePath;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class RefreshTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String PROCESSING_URI = ResourcePath.AUTH_REFRESH_URL;

    private final ObjectMapper objectMapper;

    public RefreshTokenAuthenticationFilter(final JwtDecoder jwtDecoder, final JwtService jwtService, final ObjectMapper objectMapper) {
        super(PROCESSING_URI);
        setAuthenticationManager(new ProviderManager(new JwtAuthenticationProvider(jwtDecoder)));
        setAuthenticationSuccessHandler(new TokenReissueSuccessHandler(jwtService, objectMapper));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException, BadRequestException {
        String refreshTokenValue = extractRefreshToken(request);
        return getAuthenticationManager().authenticate(new BearerTokenAuthenticationToken(refreshTokenValue));
    }

    @Override
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

    private String extractRefreshToken(HttpServletRequest request) throws BadRequestException {
        if (request.getCookies() == null) {
            throw new BadCredentialsException("token not found");
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN))
                .findAny()
                .map(Cookie::getValue)
                .orElseThrow(() -> new BadCredentialsException("token not found"));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        throw exception;
    }

}
