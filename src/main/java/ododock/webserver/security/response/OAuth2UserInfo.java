package ododock.webserver.security.response;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserInfo {

    String getAccountId();
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    String getNickname();
    String getAge();
    String getGender();
    String getBirthday();
    String getBirthYear();
    String getProfileImage();
    void addAttribute(String key, Object value);

}
