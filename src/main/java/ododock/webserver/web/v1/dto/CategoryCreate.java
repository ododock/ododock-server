package ododock.webserver.web.v1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.lang.Nullable;

@Builder
public record CategoryCreate(
        @NotBlank
        String name,
        @Nullable
        Boolean visibility
) {
}
