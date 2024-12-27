package ododock.webserver.web.v1.dto.response.account;

import lombok.Builder;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.ProfileImage;
import ododock.webserver.domain.account.SocialAccount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record AccountDetailsResponse(
        Long accountId,
        String email,
        String nickname,
        String fullname,
        LocalDate birthDate,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate,
        Set<String> providers,
        ProfileImage profileImage,
        boolean emailVerified
) {
    public static AccountDetailsResponse of(final Account account) {
        return AccountDetailsResponse.builder()
                .accountId(account.getId())
                .email(account.getEmail())
                .fullname(account.getOwnProfile().getFullname())
                .birthDate(account.getOwnProfile().getBirthDate())
                .nickname(account.getOwnProfile().getNickname())
                .profileImage(account.getOwnProfile().getProfileImage())
                .createdDate(account.getCreatedDate())
                .lastModifiedDate(account.getLastModifiedAt())
                .providers(account.getSocialAccounts().stream()
                        .map(SocialAccount::getProvider)
                        .collect(Collectors.toSet()))
                .emailVerified(account.getEmailVerified())
                .build();
    }
}
