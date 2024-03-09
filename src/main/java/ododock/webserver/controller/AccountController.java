package ododock.webserver.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ododock.webserver.request.AccountCreate;
import ododock.webserver.request.AccountPasswordUpdate;
import ododock.webserver.response.AccountCreateResponse;
import ododock.webserver.response.AccountDetailsResponse;
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

    @GetMapping("/api/v1/accounts/username")
    public Boolean validateUsername(
            @RequestParam("username") final String username
    ) {
        return accountService.validateUsername(username);
    }

    @GetMapping("/api/v1/accounts/email")
    public Boolean validateEmail(
            @RequestParam("email") final String email
    ) {
        return accountService.validateEmail(email);
    }

    @PostMapping("/api/v1/accounts")
    public AccountCreateResponse createAccount(
            @Valid @RequestBody final AccountCreate request
            ) {
        return accountService.createAccount(request);
    }

    @PatchMapping("/api/v1/accounts/{accountId}/password")
    public ResponseEntity<Void> updateAccountPassword(
            @PathVariable final Long accountId,
            @Valid @RequestBody final AccountPasswordUpdate request
            ) {
        accountService.updateAccountPassword(accountId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/accounts/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable final Long accountId
    ) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }

}
