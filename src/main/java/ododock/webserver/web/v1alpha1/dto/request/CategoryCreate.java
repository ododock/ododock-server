package ododock.webserver.web.v1alpha1.dto.request;

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
