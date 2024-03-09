package ododock.webserver.security;

public final class SecurityConstraints {
    public static final long JWT_EXPIRATION = 70000;

    private SecurityConstraints() {
        throw new UnsupportedOperationException();
    }
}
