package ododock.webserver.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;

public class CustomOAuth2AuthenticationToken extends OAuth2AuthenticationToken {

    @Getter
    @Setter
    private Long accountId;

    @Getter
    @Setter
    private Long profileId;

    /**
     * Constructs an {@code OAuth2AuthenticationToken} using the provided parameters.
     *
     * @param principal                      the user {@code Principal} registered with the OAuth 2.0 Provider
     * @param authorities                    the authorities granted to the user
     * @param authorizedClientRegistrationId the registration identifier of the
     *                                       {@link OAuth2AuthorizedClient Authorized Client}
     */
    public CustomOAuth2AuthenticationToken(OAuth2User principal,
                                           Collection<? extends GrantedAuthority> authorities,
                                           String authorizedClientRegistrationId) {
        super(principal, authorities, authorizedClientRegistrationId);
    }

}
