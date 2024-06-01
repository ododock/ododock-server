package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ProfileCreate(
        @NotBlank
        String nickname,
        @NotBlank
        String imageSource,
        @NotBlank
        String fileType
) {
}
