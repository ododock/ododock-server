package ododock.webserver.web.v1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.lang.Nullable;

@Builder
public record ProfileUpdate(
        @NotBlank
        String nickname,
        @Nullable
        String profileImageSource,
        @Nullable
        String profileImageFileType
) {
}
