package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ododock.webserver.domain.content.Genre;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Builder
public record ContentCreate (
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        Genre genre,
        @NotNull
        @NotBlank
        LocalDate publishedDate,
        @Nullable
        String isbn,
        @Nullable
        String director

) {
}
