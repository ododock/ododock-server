package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProfileUpdate(
        @NotNull
        Long profileId,
        @NotBlank
        String nickname,
        @NotBlank
        String imageSource,
        @NotBlank
        String fileType
) {
}
