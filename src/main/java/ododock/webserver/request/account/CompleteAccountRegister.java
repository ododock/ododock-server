package ododock.webserver.request.account;

import lombok.Builder;

@Builder
public record CompleteAccountRegister(
        String nickname,
        String fullname,
        String password
) {
}
