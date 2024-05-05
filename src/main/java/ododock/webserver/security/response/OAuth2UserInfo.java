package ododock.webserver.security.response;

import java.util.Map;

public interface OAuth2UserInfo {

    Map<String, Object> getAttributes();

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
