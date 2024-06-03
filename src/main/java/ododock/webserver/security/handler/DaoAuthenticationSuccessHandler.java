package ododock.webserver.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.account.TokenRecord;
import ododock.webserver.security.DaoUserDetails;
import ododock.webserver.security.response.Token;
import ododock.webserver.security.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
public class DaoAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication) throws IOException {
        writeAuthenticationToken(response, authentication);
    }

    private void writeAuthenticationToken(final HttpServletResponse response, final Authentication authentication) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.getWriter().write(convertToken(response, authentication));
    }

    private String convertToken(final HttpServletResponse response, final Authentication authentication) throws JsonProcessingException {
        final TokenRecord tokenRecord = jwtService.generateToken(authentication);
        final Long accountId = ((DaoUserDetails)authentication.getPrincipal()).getAccountId();
        final Cookie cookie = new Cookie("access_token", tokenRecord.getAccessTokenValue());
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");

        response.addCookie(cookie);

        return objectMapper.writeValueAsString(
                Token.builder()
                        .sub(String.valueOf(accountId))
                        .accessToken(tokenRecord.getAccessTokenValue())
                        .refreshToken(tokenRecord.getRefreshTokenValue())
                        .build()
        );
    }

}
