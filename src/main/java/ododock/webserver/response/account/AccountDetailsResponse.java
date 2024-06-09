package ododock.webserver.response.account;

import lombok.Builder;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.SocialAccount;
import ododock.webserver.domain.profile.ProfileImage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record AccountDetailsResponse(
        Long sub,
        Long profileId,
        String email,
        String fullname,
        LocalDate birthDate,
        boolean isDaoSignedUp,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate,
        Set<String> providers,
        String nickname,
        ProfileImage profileImage
) {
    public static AccountDetailsResponse of(final Account account) {
        return AccountDetailsResponse.builder()
                .sub(account.getId())
                .profileId(account.getOwnProfile().getId())
                .email(account.getEmail())
                .nickname(account.getOwnProfile().getNickname())
                .birthDate(account.getBirthDate())
                .fullname(account.getFullname())
                .profileImage(account.getOwnProfile().getProfileImage())
                .createdDate(account.getCreatedDate())
                .lastModifiedDate(account.getLastModifiedAt())
                .isDaoSignedUp(account.getIsDaoSignedUp())
                .providers(account.getSocialAccounts().stream()
                        .map(SocialAccount::getProvider)
                        .collect(Collectors.toSet()))
                .build();
    }
}
