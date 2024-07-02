package ododock.webserver.exception;

import ododock.webserver.domain.account.SocialAccountProvider;

public class SocialProviderDuplicatedException extends AbstractDomainException {

    public SocialProviderDuplicatedException(final Object resource, final String resourceName) {
        super(ErrorCode.RESOURCE_NOT_FOUNDS, "Resource " + resource.getClass().getSimpleName() + " " + resourceName + " not founds");
    }

    public SocialProviderDuplicatedException(final Long accountId, final SocialAccountProvider provider) {
        super(ErrorCode.RESOURCE_NOT_FOUNDS, "Account " + accountId + " already connect social account with " + provider);
    }

}
