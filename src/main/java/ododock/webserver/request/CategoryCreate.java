package ododock.webserver.request;

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
