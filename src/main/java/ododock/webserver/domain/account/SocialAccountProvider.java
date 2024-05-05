package ododock.webserver.domain.account;

public enum SocialAccountProvider {
    GOOGLE("google"),
    NAVER("naver");

    private String provider;

    SocialAccountProvider(final String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }

}
