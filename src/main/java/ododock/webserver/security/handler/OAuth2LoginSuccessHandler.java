package ododock.webserver.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.TokenRecord;
import ododock.webserver.security.response.Token;
import ododock.webserver.security.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${jwt.redirect:http://localhost:8080}")
    private String REDIRECT_URI;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        writeAuthenticationToken(response, convertToken((OAuth2AuthenticationToken) authentication));
        String redirectUri = String.format(REDIRECT_URI);
        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }


    private String convertToken(final OAuth2AuthenticationToken token) throws JsonProcessingException {
        final TokenRecord tokenRecord = jwtService.generateToken(token);
        return objectMapper.writeValueAsString(
                Token.builder()
                        .accessToken(tokenRecord.getAccessTokenValue())
                        .refreshToken(tokenRecord.getRefreshTokenValue())
                        .build()
        );
    }

    private void writeAuthenticationToken(HttpServletResponse response, final String token) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.getWriter().write(token);
    }

}
