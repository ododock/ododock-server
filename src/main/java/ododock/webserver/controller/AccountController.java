package ododock.webserver.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ododock.webserver.request.AccountCreate;
import ododock.webserver.request.AccountPasswordUpdate;
import ododock.webserver.response.AccountCreateResponse;
import ododock.webserver.response.ApiResponse;
import ododock.webserver.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/api/v1/accounts/{accountId}")
    public ResponseEntity<Void> getAccount(
            final @PathVariable Long accountId
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/accounts")
    public AccountCreateResponse createAccount(
            final @Valid @RequestBody AccountCreate request
            ) {
        return accountService.createAccount(request);
    }

    @PatchMapping("/api/v1/accounts/{accountId}/password")
    public ResponseEntity<Void> updateAccountPassword(
            final @PathVariable Long accountId,
            final @RequestBody AccountPasswordUpdate request
            ) {
        accountService.updateAccountPassword(accountId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/accounts/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            final @PathVariable Long accountId
    ) {
        return ResponseEntity.ok().build();
    }

}
