package ododock.webserver.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AccountDetailsResponse(
        Long id,
        String email,
        String username,
        String fullname,
        String birthDate,
        boolean enabled,
        boolean accountLocked,
        boolean accountExpired,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {

}
