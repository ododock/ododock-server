package ododock.webserver.web.v1alpha1;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.account.AccountQueryService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.dto.account.AccountSocialConnectDetails;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ResourcePath.API + ResourcePath.API_VERSION)
@RequiredArgsConstructor
public class V1alpha1AccountQueryController {

    private final AccountQueryService accountQueryService;

    @GetMapping(
            value = ResourcePath.ACCOUNTS + "/{" + ResourcePath.PATH_VAR_ID + "}"
    )
    public V1alpha1Account getAccountDetails(
            @PathVariable final Long id
    ) {
        return V1alpha1Account.toControllerDto(accountQueryService.getAccountDetails(id));
    }

    @GetMapping(
            value = ResourcePath.ACCOUNTS + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_SOCIAL_ACCOUNTS
    )
    public AccountSocialConnectDetails getAccountSocialConnectDetails(@PathVariable final Long id) {
        return accountQueryService.getAccountSocialConnectDetails(id);
    }

}
