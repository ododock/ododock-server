package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ProfileImageUpdate(
        @NotBlank
        String imageSource,
        @NotBlank
        String fileType
) {
}
