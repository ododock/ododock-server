package ododock.webserver.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.security.TokenRecord;
import ododock.webserver.security.response.UserPrincipal;
import ododock.webserver.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.security.oauth-callback-uri:http://localhost:3000/oauth2/callback?sub=%s&access_token=%s&refresh_token=%s}")
    private String OAUTH_CALLBACK_URI = "http://localhost:3000/oauth2/callback?sub=%s&provider=%s&access_token=%s&refresh_token=%s";

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication
    ) throws IOException {
        final OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        final TokenRecord tokenRecord = jwtService.generateToken(UserPrincipal.from(oauthToken));
        final String redirectUri = String.format(
                OAUTH_CALLBACK_URI,
                tokenRecord.getAccountId(),
                oauthToken.getAuthorizedClientRegistrationId(),
                tokenRecord.getAccessTokenValue(),
                tokenRecord.getRefreshTokenValue());
        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }

}