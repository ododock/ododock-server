package ododock.webserver.web.v1.dto;

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
