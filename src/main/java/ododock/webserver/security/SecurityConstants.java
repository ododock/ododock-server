package ododock.webserver.security;

public final class SecurityConstants {
    public static final long JWT_ACCESS_EXPIRATION = 70000;
    public static final long JWT_REFRESH_EXPIRATION = 140000;

    private SecurityConstants() {
        throw new UnsupportedOperationException();
    }
}
