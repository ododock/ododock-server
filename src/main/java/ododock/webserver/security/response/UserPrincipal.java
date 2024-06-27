package ododock.webserver.security.response;

import lombok.Builder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;

@Builder
public record UserPrincipal(String id, String provider) {

    public static UserPrincipal from(OAuth2User user) {
        return UserPrincipal.builder()
                .id(user.getAttribute("accountId"))
                .provider(user.getAttribute("authorizedClientRegistrationId"))
                .build();
    }

    public static UserPrincipal from(DaoUserDetails user) {
        return UserPrincipal.builder()
                .id(String.valueOf(user.getAccountId()))
                .provider("oddk")
                .build();
    }

    public static UserPrincipal from(Jwt token) {
        return UserPrincipal.builder()
                .id(token.getId())
                .provider(String.valueOf(token.getIssuer()))
                .build();
    }

}
