package ododock.webserver.security.util;

import ododock.webserver.security.response.GoogleUserInfo;
import ododock.webserver.security.response.NaverUserInfo;
import ododock.webserver.security.response.OAuth2UserInfo;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.stream.Collectors;

public class OAuth2UserMapper {

    public static OAuth2UserInfo resolveUserInfo(final OAuth2UserRequest request, final OAuth2User user) {
        String registrationId = request.getClientRegistration().getRegistrationId();
        switch (registrationId) {
            case "naver":
                return new NaverUserInfo(user.getAttributes());
            case "google":
                return new GoogleUserInfo(user.getAttributes());
            default:
                throw new IllegalArgumentException("unsupported oauth provider");
        }
    }

    public static Map<String, String> resolveAttributes(final OAuth2AuthenticationToken authentication) {
        Map<String, Object> attributes = null;
        switch (authentication.getAuthorizedClientRegistrationId()) {
            case "naver":
                attributes = (Map<String, Object>) authentication.getPrincipal().getAttributes().get("response");
                break;
            case "google":
                attributes = authentication.getPrincipal().getAttributes();
                break;
        }
        return attributes.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.valueOf(entry.getValue())
                ));
    }

    private OAuth2UserMapper() {
        throw new UnsupportedOperationException();
    }
}
