package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;

public record Login(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
