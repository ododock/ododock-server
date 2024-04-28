package ododock.webserver.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.account.TokenRecord;
import ododock.webserver.security.response.Token;
import ododock.webserver.security.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
public class DaoAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            final Authentication authentication) throws IOException {
        writeAuthenticationToken(response, authentication);
    }

    private HttpServletResponse writeAuthenticationToken(HttpServletResponse response, final Authentication authentication) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.getWriter().write(convertToken(authentication));
        return response;
    }

    private String convertToken(final Authentication authentication) throws JsonProcessingException {
        final TokenRecord tokenRecord = jwtService.generateToken(authentication);
        return objectMapper.writeValueAsString(
                Token.builder()
                        .accessToken(tokenRecord.getAccessTokenValue())
                        .refreshToken(tokenRecord.getRefreshTokenValue())
                        .build()
        );
    }

}
