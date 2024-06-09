package ododock.webserver.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class RefreshTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String PROCESSING_URI = "/api/v1/login/refresh";

    private final ObjectMapper objectMapper;

    public RefreshTokenAuthenticationFilter(final JwtDecoder jwtDecoder, final ObjectMapper objectMapper) {
        super(PROCESSING_URI);
        setAuthenticationManager(new ProviderManager(new JwtAuthenticationProvider(jwtDecoder)));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh_token")) {
                    String token = cookie.getValue();
                    return getAuthenticationManager().authenticate(new BearerTokenAuthenticationToken(token));
                }
            }
        }
        return null;
    }


}
