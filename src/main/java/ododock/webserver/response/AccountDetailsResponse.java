package ododock.webserver.response;

import lombok.Builder;
import ododock.webserver.domain.account.Account;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record AccountDetailsResponse(
        Long id,
        String email,
        String username,
        String fullname,
        LocalDate birthDate,
        boolean enabled,
        boolean accountLocked,
        boolean accountExpired,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
    public static AccountDetailsResponse of(final Account account) {
        return AccountDetailsResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .email(account.getEmail())
                .birthDate(account.getBirthDate())
                .fullname(account.getFullname())
                .createdDate(account.getCreatedDate())
                .lastModifiedDate(account.getLastModifiedDate())
                .enabled(account.getEnabled())
                .accountExpired(account.getAccountExpired())
                .accountLocked(account.getAccountLocked())
                .build();
    }
}
