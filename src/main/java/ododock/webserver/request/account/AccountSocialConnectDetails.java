package ododock.webserver.request.account;

import lombok.Builder;
import ododock.webserver.domain.account.Account;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Builder
public record AccountSocialConnectDetails(
        Long accountId,
        List<SocialAccountDetail> socialAccountDetails
) {
    public static AccountSocialConnectDetails of(final Account account) {
        return AccountSocialConnectDetails.builder()
                .accountId(account.getId())
                .socialAccountDetails(
                        Optional.ofNullable(account.getSocialAccounts())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(SocialAccountDetail::of)
                                .toList()
                )
                .build();
    }
}
