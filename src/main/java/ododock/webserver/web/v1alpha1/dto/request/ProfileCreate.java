package ododock.webserver.web.v1alpha1.dto.request;

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
