package ododock.webserver.exception;

public class ResourceNotFoundException extends AbstractDomainException {

    public ResourceNotFoundException(final Class<?> resource, final String resourceName) {
        super(ErrorCode.RESOURCE_NOT_FOUNDS,
                "Resource " + resource.getSimpleName() + " " + resourceName + " not founds");
    }

    public ResourceNotFoundException(final Class<?> resource, final Object subresource, final String resourceName) {
        super(ErrorCode.RESOURCE_NOT_FOUNDS,
                "Resource " + resource.getSimpleName() + " " + subresource.getClass().getSimpleName() + " " + resourceName + " not founds");
    }

    public ResourceNotFoundException(final Class<?> resource, final Long resourceId) {
        super(ErrorCode.RESOURCE_NOT_FOUNDS,
                "Resource " + resource.getSimpleName() + " " + resourceId.toString() + " not founds");
    }

    public ResourceNotFoundException(final Class<?> resource, final Class<?> subresource, final Long resourceId) {
        super(ErrorCode.RESOURCE_NOT_FOUNDS,
                "Resource " + resource.getSimpleName() + " " + subresource.getSimpleName() + " with id " + resourceId + " not founds");
    }

}
