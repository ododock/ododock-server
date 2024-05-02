package ododock.webserver.security.response;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getAccountId() {
        return (String) attributes.get("accountId");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getNickname() {
        String nickname = (String) attributes.get("nickname");
        return nickname == null
                ? null
                : nickname;
    }

    @Override
    public String getAge() {
        return (String) attributes.get("age");
    }

    /**
     * return user gender as F for female, M for male and U for unidentified.
     *
     * @return
     */
    @Override
    public String getGender() {
        String gender = (String) attributes.get("gender");
        return gender == null ? null : gender;
    }

    /**
     * return user birthdate in MM-DD format.
     *
     * @return
     */
    @Override
    public String getBirthday() {
        return (String) attributes.get("birthday");
    }

    /**
     * return user birth year in YYYY format.
     *
     * @return
     */
    @Override
    public String getBirthYear() {
        return (String) attributes.get("birthyear");
    }

    /**
     * return user profile photo url
     *
     * @return
     */
    @Override
    public String getProfileImage() {
        return (String) attributes.get("profileImage");
    }

    @Override
    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

}
