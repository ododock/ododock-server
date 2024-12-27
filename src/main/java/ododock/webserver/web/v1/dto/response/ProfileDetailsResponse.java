package ododock.webserver.web.v1.dto.response;

import lombok.Builder;
import ododock.webserver.domain.account.Profile;
import ododock.webserver.domain.account.ProfileImage;

import java.time.LocalDate;

@Builder
public record ProfileDetailsResponse(
        Long accountId,
        String nickname,
        String fullname,
        LocalDate birthDate,
        ProfileImage profileImage
) {
    public static ProfileDetailsResponse of(final Long accountId, final Profile profile) {
        return ProfileDetailsResponse.builder()
                .accountId(accountId)
                .nickname(profile.getNickname())
                .fullname(profile.getFullname())
                .profileImage(profile.getProfileImage())
                .build();
    }
}
