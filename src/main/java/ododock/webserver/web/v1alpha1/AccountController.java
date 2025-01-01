package ododock.webserver.web.v1alpha1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.account.AccountManageService;
import ododock.webserver.domain.account.AccountService;
import ododock.webserver.domain.account.SocialAccountService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.exception.InvalidVerificationCodeException;
import ododock.webserver.web.exception.VerificationCodeExpiredException;
import ododock.webserver.web.v1alpha1.dto.account.*;
import ododock.webserver.web.v1alpha1.dto.response.ValidateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountManageService accountManageService;
    private final SocialAccountService socialAccountService;

    @GetMapping(
            value = "",
            params = "email"
    )
    public ValidateResponse validateEmail(
            final @RequestParam(value = "email", required = false) String email) {
        return ValidateResponse.of(accountService.isAvailableEmail(email));
    }

    @GetMapping(
            value = "",
            params = "nickname"
    )
    public ValidateResponse validateNickname(
            final @RequestParam("nickname") String nickname
    ) {
        return ValidateResponse.of(accountService.isAvailableNickname(nickname));
    }

    @PostMapping(
            value = ""
    )
    public ResponseEntity<Void> createDaoAccount(
            final @Valid @RequestBody AccountCreate request) throws Exception {
        accountService.createDaoAccount(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(
            value = ResourcePath.VERIFICATION
    )
    public ResponseEntity<Void> verifyDaoAccountEmail(
            final @RequestBody CompleteDaoAccountVerification request) throws InvalidVerificationCodeException, VerificationCodeExpiredException {
        accountManageService.verifyDaoAccountEmail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_SOCIAL_ACCOUNTS)
    public ResponseEntity<Void> mergeSocialAccount(
            final @PathVariable Long id,
            final @RequestBody OAuthAccountMerge request) {
        socialAccountService.mergeSocialAccount(id, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}"
    )
    public ResponseEntity<Void> socialAccountRegisterComplete(
            final @PathVariable Long id,
            final @Valid @RequestBody CompleteSocialAccountRegister request) {
        socialAccountService.completeSocialAccountRegister(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PASSWORD
    )
    public ResponseEntity<Void> updateAccountPassword(
            final @PathVariable Long id,
            final AccountPasswordUpdate request) {
        accountService.updateAccountPassword(id, request);
        return ResponseEntity.ok().build();
    }

    /**
     * @param request
     * @return
     * @throws InvalidVerificationCodeException
     * @throws VerificationCodeExpiredException
     */
    @PutMapping(
            value = "/{" + ResourcePath.PATH_VAR_NAME + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PASSWORD
    )
    public ResponseEntity<Void> resetPassword(
            final @PathVariable String name,
            final @Valid @RequestBody AccountPasswordReset request) throws InvalidVerificationCodeException, VerificationCodeExpiredException {
        accountManageService.resetAccountPassword(name, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}"
    )
    public ResponseEntity<Void> deleteAccount(
            final @PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_SOCIAL_ACCOUNTS + "/{" + ResourcePath.PATH_VAR_SUB_ID + "}"
    )
    public ResponseEntity<Void> deleteSocialAccounts(
            final @PathVariable Long id,
            final @PathVariable Long subId) {
        socialAccountService.deleteConnectedSocialAccount(id, subId);
        return ResponseEntity.ok().build();
    }

}
