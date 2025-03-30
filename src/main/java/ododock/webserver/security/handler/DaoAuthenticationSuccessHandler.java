package ododock.webserver.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ododock.webserver.security.JwtService;
import ododock.webserver.security.response.DaoUserDetails;
import ododock.webserver.security.response.UserPrincipal;
import ododock.webserver.security.response.V1alpha1Token;
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
        DaoUserDetails userDetails = (DaoUserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(UserPrincipal.from(userDetails));
        String refreshToken = jwtService.generateRefreshToken(UserPrincipal.from(userDetails));

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

        return objectMapper.writeValueAsString(
                V1alpha1Token.builder()
                        .sub(String.valueOf(userDetails.getAccountId()))
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );
    }

}
