package ododock.webserver.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.security.JwtService;
import ododock.webserver.security.handler.TokenReissueSuccessHandler;
import ododock.webserver.web.ResourcePath;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(REFRESH_TOKEN)) {
                    String token = cookie.getValue();
                    return getAuthenticationManager().authenticate(new BearerTokenAuthenticationToken(token));
                }
            }
        } else {
            if (request.getHeader(REFRESH_TOKEN) != null) {
                if (request.getHeader(REFRESH_TOKEN).isEmpty()) {
                    throw new BadJwtException("Token not found from http request headers");
                }
                log.info("try to authenticate from RefreshToken");
                return getAuthenticationManager().authenticate(new BearerTokenAuthenticationToken(request.getHeader(REFRESH_TOKEN)));
            }
        }
        return null;
    }

    @Override
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

}
