package ododock.webserver.web.v1alpha1.dto.response;

import lombok.Builder;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Builder
public record ContentDetailsResponse(
        Long id,
        LocalDate publishedDate,
        String name,
        @Nullable
        Long isbn,
        @Nullable
        String director
) {
}
