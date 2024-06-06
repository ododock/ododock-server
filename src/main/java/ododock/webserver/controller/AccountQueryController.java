package ododock.webserver.controller;

import lombok.RequiredArgsConstructor;
import ododock.webserver.request.account.AccountSocialConnectDetails;
import ododock.webserver.response.account.AccountDetailsResponse;
import ododock.webserver.service.AccountQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountQueryController {

    private final AccountQueryService accountQueryService;

    @GetMapping("/api/v1/accounts/{accountId}")
    public AccountDetailsResponse getAccountDetails(
            @PathVariable final Long accountId
    ) {
        return accountQueryService.getAccountDetails(accountId);
    }

    @GetMapping("api/v1/accounts/{accountId}/social-accounts")
    public AccountSocialConnectDetails getAccountSocialConnectDetails(@PathVariable final Long accountId) {
        return accountQueryService.getAccountSocialConnectDetails(accountId);
    }

}
