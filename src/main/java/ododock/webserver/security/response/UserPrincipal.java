package ododock.webserver.security.response;

import lombok.Builder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;

@Builder
public record UserPrincipal(String id, String provider) {

    public static UserPrincipal from(OAuth2AuthenticationToken token) {
        if (token.getAuthorizedClientRegistrationId().equals("naver")) {
            java.util.Map<String, Object> attributes = (Map<String, Object>) token.getPrincipal().getAttributes().get("response");
            String accountId = attributes.get("accountId").toString();
            return UserPrincipal.builder()
                    .id(accountId)
                    .provider("naver")
                    .build();
        }

        if (token.getAuthorizedClientRegistrationId().equals("google")) {
            String accountId = token.getPrincipal().getAttributes().get("accountId").toString();
            return UserPrincipal.builder()
                    .id(accountId)
                    .provider("google")
                    .build();
        }

        throw new IllegalArgumentException("not supported oauth2 provider");
    }

    public static UserPrincipal from(DaoUserDetails user) {
        return UserPrincipal.builder()
                .id(String.valueOf(user.getAccountId()))
                .provider("dev.oddk.xyz")
                .build();
    }

    public static UserPrincipal from(Jwt token) {
        return UserPrincipal.builder()
                .id(token.getId())
                .provider(String.valueOf(token.getIssuer()))
                .build();
    }

}
