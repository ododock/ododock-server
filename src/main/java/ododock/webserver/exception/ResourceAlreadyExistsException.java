package ododock.webserver.exception;

public class ResourceAlreadyExistsException extends AbstractDomainException {

    public ResourceAlreadyExistsException(final Object resource, final String resourceContent) {
        super(ErrorCode.RESOURCE_ALREADY_EXISTS,
                "Resource "+ resource.getClass().toString() + " " + resourceContent + " already exists");
    }

    public ResourceAlreadyExistsException(final Object resource, final Long resourceId) {
        super(ErrorCode.RESOURCE_ALREADY_EXISTS,
                "Resource "+ resource.getClass().toString() + " " + resourceId.toString() + " already exists");
    }

}
