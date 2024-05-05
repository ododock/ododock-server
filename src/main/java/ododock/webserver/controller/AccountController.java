package ododock.webserver.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ododock.webserver.request.AccountCreate;
import ododock.webserver.request.AccountPasswordUpdate;
import ododock.webserver.request.OAuthAccountConnect;
import ododock.webserver.response.AccountCreateResponse;
import ododock.webserver.response.AccountDetailsResponse;
import ododock.webserver.response.ValidateResponse;
import ododock.webserver.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/api/v1/accounts/{accountId}")
    public AccountDetailsResponse getAccount(
            @PathVariable final Long accountId
    ) {
        return accountService.getAccount(accountId);
    }

    @GetMapping("/api/v1/accounts")
    public ValidateResponse validateEmail(
            final @RequestParam(value = "email", required = false) String email
    ) {
        return ValidateResponse.of(accountService.isAvailableEmail(email));
    }

    @PostMapping("/api/v1/accounts")
    public AccountCreateResponse createDaoAccount(
            final @Valid @RequestBody AccountCreate request
    ) {
        return accountService.createDaoAccount(request);
    }

    @PostMapping("/api/v1/accounts/{accountId}/social-accounts")
    public ResponseEntity<String> connectSocialAccount(
            final @PathVariable Long accountId,
            final @RequestBody OAuthAccountConnect request
    ) {
        accountService.connectSocialAccount(accountId, request);
        return null;
    }

    @PatchMapping("/api/v1/accounts/{accountId}/password")
    public ResponseEntity<Void> updateAccountPassword(
            final @PathVariable Long accountId,
            final @Valid @RequestBody AccountPasswordUpdate request
    ) {
        accountService.updateAccountPassword(accountId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/accounts/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            final @PathVariable Long accountId
    ) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/accounts/{accountId}/social-accounts/{socialAccountId}")
    public ResponseEntity<Void> deleteSocialAccounts(
            final @PathVariable("accountId") Long accountId,
            final @PathVariable("socialAccountId") Long socialAccountId
    ) {
        accountService.deleteConnectedSocialAccount(accountId, socialAccountId);
        return ResponseEntity.ok().build();
    }

}
