package ododock.webserver.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ododock.webserver.security.response.Token;
import ododock.webserver.security.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class DaoAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
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
        return objectMapper.writeValueAsString(
                Token.builder()
                        .accessToken(jwtService.generateAccessToken(authentication))
                        .refreshToken(jwtService.generateRefreshToken(authentication))
                        .build()
        );
    }

}
