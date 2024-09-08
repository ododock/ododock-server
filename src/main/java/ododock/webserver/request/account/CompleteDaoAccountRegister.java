package ododock.webserver.request.account;

import lombok.Builder;

@Builder
public record CompleteDaoAccountRegister(
        String email,
        String code
) {
}
