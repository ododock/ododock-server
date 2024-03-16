package ododock.webserver.response;

import lombok.Builder;
import ododock.webserver.domain.account.Account;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record AccountDetailsResponse(
        Long id,
        String email,
        String fullname,
        LocalDate birthDate,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialNonExpired,
        boolean enabled,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
    public static AccountDetailsResponse of(final Account account) {
        return AccountDetailsResponse.builder()
                .id(account.getId())
                .email(account.getEmail())
                .birthDate(account.getBirthDate())
                .fullname(account.getFullname())
                .createdDate(account.getCreatedDate())
                .lastModifiedDate(account.getLastModifiedDate())
                .accountNonExpired(account.getAccountNonExpired())
                .accountNonLocked(account.getAccountNonLocked())
                .credentialNonExpired(account.getCredentialNonExpired())
                .enabled(account.getEnabled())
                .build();
    }
}
