package ododock.webserver.response;

import lombok.Builder;
import ododock.webserver.domain.profile.Category;
import ododock.webserver.domain.profile.Profile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
public record ProfileDetailsResponse(
        Long profileId,
        String nickname,
        String imageSource,
        List<Category> categories,
        String fileType,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
    public static ProfileDetailsResponse of(final Profile profile) {
        return ProfileDetailsResponse.builder()
                .profileId(profile.getId())
                .nickname(profile.getNickname())
                .categories(profile.getCategories())
                .imageSource(profile.getProfileImage().getFileType())
                .fileType(profile.getProfileImage().getFileType())
                .build();
    }
}
