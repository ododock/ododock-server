package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import ododock.webserver.domain.profile.ProfileImage;

@Builder
public record ProfileUpdate(
        @NotBlank
        Long profileId,
        @NotBlank
        String nickname,
        @NotBlank
        String imageSource,
        @NotBlank
        String fileType
) {
}
