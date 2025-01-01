package ododock.webserver.web.v1alpha1.dto.account;

import lombok.Builder;

@Builder
public record CompleteSocialAccountRegister(
        String nickname,
        String fullname,
        String password
) {
}
