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
        public static ProfileCreate of(final AccountCreate request) {
                return ProfileCreate.builder()
                        .nickname(request.nickname())
                        .imageSource(request.imageSource())
                        .fileType(request.fileType())
                        .build();
        }
}
