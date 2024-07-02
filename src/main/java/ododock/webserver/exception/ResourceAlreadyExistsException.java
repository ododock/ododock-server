package ododock.webserver.exception;

import org.springframework.lang.Nullable;

public class ResourceAlreadyExistsException extends AbstractDomainException {

    private static final String MESSAGE_FORMAT = "Resource[%s] Subresource[%s] value[%s] already exists";

    public ResourceAlreadyExistsException(final Class<?> resource, final String subresource, final String value) {
        super(ErrorCode.RESOURCE_ALREADY_EXISTS,
                String.format(MESSAGE_FORMAT, resource.getSimpleName(), subresource, value));
    }

}
