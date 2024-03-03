package ododock.webserver.response;

import lombok.Builder;
import ododock.webserver.domain.content.Genre;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Builder
public record ContentDetailsResponse(
        Long id,
        LocalDate publishedDate,
        String name,
        Genre genre,
        @Nullable
        Long isbn,
        @Nullable
        String director
) {
}
