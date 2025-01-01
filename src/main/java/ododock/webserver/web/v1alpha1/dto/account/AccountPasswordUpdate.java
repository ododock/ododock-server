package ododock.webserver.web.v1alpha1.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountPasswordUpdate(
        @NotBlank
        String password
) {
}
