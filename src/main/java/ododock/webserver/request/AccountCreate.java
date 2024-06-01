package ododock.webserver.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
public record AccountCreate (
        @NotBlank
        String email,
        @NotBlank
        String password,
        @Nullable
        String fullname,
        @Nullable
        @JsonDeserialize(using = LocalDateDeserializer.class)
        LocalDate birthDate,
        @Nullable
        String nickname,
        @Nullable
        Map<String, List<String>> attributes
) {
}
