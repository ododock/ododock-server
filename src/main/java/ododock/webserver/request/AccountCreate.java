package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AccountCreate (
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String fullname,
        @NotNull
        LocalDate birthDate,
        @NotBlank
        String nickname,
        @NotBlank
        String imageSource,
        @NotBlank
        String fileType
) {
}
