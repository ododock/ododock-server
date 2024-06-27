package ododock.webserver.security.response;

import lombok.Builder;
import ododock.webserver.security.DaoUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Builder
public record UserPrincipal(
        String id,
        String provider,
        String roles
) {
    public static UserPrincipal from(OAuth2User user) {
        return UserPrincipal.builder()
                .id(user.getAttribute("accountId"))
                .provider(user.getAttribute("authorizedClientRegistrationId"))
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().toString())
                .build();
    }

    public static UserPrincipal from(DaoUserDetails user) {
        return UserPrincipal.builder()
                .id(String.valueOf(user.getAccountId()))
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().toString())
                .provider("oddk")
                .build();
    }
}
