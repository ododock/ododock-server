package ododock.webserver.response;

import lombok.Builder;
import ododock.webserver.domain.profile.Profile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record ProfileDetailsResponse(
        Long profileId,
        Long ownerAccountId,
        String nickname,
        String imageSource,
        List<CategoryDetailsResponse> categories,
        String fileType,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
    public static ProfileDetailsResponse of(final Profile profile) {
        return ProfileDetailsResponse.builder()
                .profileId(profile.getId())
                .ownerAccountId(profile.getOwnerAccount().getId())
                .nickname(profile.getNickname())
                .categories(profile.getCategories().stream()
                        .map(CategoryDetailsResponse::of)
                        .collect(Collectors.toList()))
                .imageSource(profile.getProfileImage().getImageSource())
                .fileType(profile.getProfileImage().getFileType())
                .build();
    }
}
