package ododock.webserver.domain.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER");

    private final String roleName;

}
