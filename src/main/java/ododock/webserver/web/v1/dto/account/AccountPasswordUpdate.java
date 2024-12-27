package ododock.webserver.web.v1.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountPasswordUpdate(
        @NotBlank
        String password
) {
}
