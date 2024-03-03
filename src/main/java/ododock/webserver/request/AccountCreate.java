package ododock.webserver.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AccountCreate (
        @NotNull
        String email,
        @NotNull
        String username,
        @NotNull
        String password,
        @NotNull
        String fullname,
        @NotNull
        LocalDate birthDate,
        @NotNull
        String nickname,
        @NotNull
        String imageSource,
        @NotNull
        String fileType
) {
}
