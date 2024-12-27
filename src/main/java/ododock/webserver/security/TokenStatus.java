package ododock.webserver.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenStatus {

    ACTIVE("ACTIVE"),
    REVOKED("REVOKED"),
    EXPIRED("EXPIRED");

    private final String statusName;

}
