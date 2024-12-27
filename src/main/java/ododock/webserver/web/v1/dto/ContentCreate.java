package ododock.webserver.web.v1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Builder
public record ContentCreate (
        @NotBlank
        String name,
        @NotNull
        LocalDate publishedDate,
        @Nullable
        String isbn,
        @Nullable
        String director

) {
}
