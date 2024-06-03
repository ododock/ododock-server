package ododock.webserver.security.response;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class GoogleUserInfo implements OAuth2UserInfo {

    @Getter
    private Map<String, Object> attributes;

    public GoogleUserInfo(final Map<String, Object> attributes) {
        this.attributes = new HashMap<>(attributes);
    }

    @Override
    public String getAccountId() {
        return (String) attributes.get("accountId");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    /**
     * return sub that represents user identifier from Google.
     *
     * @return
     */
    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getNickname() {
        return null;
    }

    @Override
    public String getAge() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public String getBirthday() {
        return null;
    }

    @Override
    public String getBirthYear() {
        return null;
    }

    @Override
    public String getProfileImage() {
        return attributes.get("picture").toString();
    }

    @Override
    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

}