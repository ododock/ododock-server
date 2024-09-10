package ododock.webserver.controller;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import ododock.webserver.exception.InvalidVerificationCodeException;
import ododock.webserver.exception.VerificationCodeExpiredException;
import ododock.webserver.request.account.AccountCreate;
import ododock.webserver.request.account.AccountPasswordUpdate;
import ododock.webserver.request.account.CompleteDaoAccountRegister;
import ododock.webserver.request.account.CompleteSocialAccountRegister;
import ododock.webserver.request.account.OAuthAccountConnect;
import ododock.webserver.request.account.RequestVerificationCode;
import ododock.webserver.response.ValidateResponse;
import ododock.webserver.response.account.AccountCreateResponse;
import ododock.webserver.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/api/v1/accounts")
    public ValidateResponse validateEmail(
            final @RequestParam(value = "email", required = false) String email
    ) {
        return ValidateResponse.of(accountService.isAvailableEmail(email));
    }

    @PostMapping("/api/v1/accounts")
    public AccountCreateResponse createDaoAccount(
            final @Valid @RequestBody AccountCreate request
    ) throws Exception {
        return accountService.createDaoAccount(request);
    }

    @PutMapping("/api/v1/accounts/{accountId}/verification-code")
    public ResponseEntity<Void> sendVerificationCode(
            final @PathVariable("accountId") Long accountId,
            final @RequestParam String email
    ) throws Exception {
        System.out.println(accountId);
        accountService.sendEmailVerificationCode(accountId, email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/accounts/{accountId}/verification-code")
    public ResponseEntity<Void> verifyDaoAccount(
            final @PathVariable Long accountId,
            final @Validated @ModelAttribute CompleteDaoAccountRegister request
    ) throws InvalidVerificationCodeException, VerificationCodeExpiredException {
        accountService.activateDaoAccountRegister(accountId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/accounts/{accountId}/social-accounts")
    public ResponseEntity<Void> connectSocialAccount(
            final @PathVariable Long accountId,
            final @RequestBody OAuthAccountConnect request
    ) {
        accountService.connectSocialAccount(accountId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/v1/accounts/{accountId}")
    public ResponseEntity<Void> socialAccountRegisterComplete(
            final @PathVariable Long accountId,
            final @Valid @RequestBody CompleteSocialAccountRegister request
    ) {
        accountService.completeSocialAccountRegister(accountId, request);
        return ResponseEntity.ok().build();
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
