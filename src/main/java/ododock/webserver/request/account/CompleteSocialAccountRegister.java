package ododock.webserver.request.account;

import lombok.Builder;

@Builder
public record CompleteSocialAccountRegister(
        String nickname,
        String fullname,
        String password
) {
}
