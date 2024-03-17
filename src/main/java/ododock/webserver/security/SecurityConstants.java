package ododock.webserver.security;

public final class SecurityConstants {
    public static final long JWT_ACCESS_EXPIRATION = 600000L;
    public static final long JWT_REFRESH_EXPIRATION = 86400000L;

    private SecurityConstants() {
        throw new UnsupportedOperationException();
    }
}
