package ododock.webserver.security.response;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    @Getter
    private Map<String, Object> attributes;

    public NaverUserInfo(final Map<String, Object> attributes) {
        this.attributes = new HashMap<>(attributes);
        Map<String, Object> map = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getAccountId() {
        return ((Map<String, Object>) attributes.get("response")).get("accountId").toString();
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return ((Map<String, Object>) attributes.get("response")).get("id").toString();
    }

    @Override
    public String getEmail() {
        Object obj = ((Map<String, Object>) attributes.get("response")).get("email");
        return obj == null ? null : obj.toString();
    }

    @Override
    public String getName() {
        Object obj = ((Map<String, Object>) attributes.get("response")).get("name");
        return obj == null ? null : obj.toString();
    }

    @Override
    public String getNickname() {
        Object obj = ((Map<String, Object>) attributes.get("response")).get("nickname");
        return obj == null ? null : obj.toString();
    }

    @Override
    public String getAge() {
        return ((Map<String, Object>) attributes.get("response")).get("age").toString();
    }

    /**
     * return user gender as F for female, M for male and U for unidentified.
     *
     * @return
     */
    @Override
    public String getGender() {
        Object obj = ((Map<String, Object>) attributes.get("response")).get("gender");
        return obj == null ? null : obj.toString();
    }

    /**
     * return user birthdate in MM-DD format.
     *
     * @return
     */
    @Override
    public String getBirthday() {
        Object obj = ((Map<String, Object>) attributes.get("response")).get("birthday");
        return obj == null ? null : obj.toString();
    }

    /**
     * return user birth year in YYYY format.
     *
     * @return
     */
    @Override
    public String getBirthYear() {
        Object obj = ((Map<String, Object>) attributes.get("response")).get("birthyear");
        return obj == null ? null : obj.toString();
    }

    /**
     * return user profile photo url
     *
     * @return
     */
    @Override
    public String getProfileImage() {
        Object obj = ((Map<String, Object>) attributes.get("response")).get("profile_image");
        return obj == null ? null : obj.toString();
    }

    @Override
    public void addAttribute(String key, Object value) {
        Map<String, Object> map = (Map<String, Object>) attributes.get("response");
        map.put(key, value);
    }

}
