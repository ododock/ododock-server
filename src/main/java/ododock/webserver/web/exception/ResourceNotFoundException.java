package ododock.webserver.web.exception;

public class ResourceNotFoundException extends AbstractDomainException {

    public ResourceNotFoundException(final Object resource, final String resourceName) {
        super(ErrorCode.RESOURCE_NOT_FOUNDS, "Resource "+ resource.getClass().toString() + " " + resourceName + " not founds");
    }

    public ResourceNotFoundException(final Object resource, final Long resourceId) {
        super(ErrorCode.RESOURCE_NOT_FOUNDS, "Resource "+ resource.getClass().toString() + " " + resourceId.toString() + " not founds");
    }

    public ResourceNotFoundException(final Object resource, final String property, final Long id) {
        super(ErrorCode.RESOURCE_NOT_FOUNDS, "Resource "+ resource.getClass().toString() + " (" + property + " " + id + ") not founds");
    }

}
