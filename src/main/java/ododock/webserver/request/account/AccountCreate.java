package ododock.webserver.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
public record AccountCreate(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String nickname,
        @Nullable
        String fullname,
        @Nullable
        LocalDate birthDate,
        @Nullable
        Map<String, List<String>> attributes
) {
}
