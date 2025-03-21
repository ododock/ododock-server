package ododock.webserver.web.exceptionhandler.factories;

import ododock.webserver.web.ResourceNotFoundException;
import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactorySupport;

public class ResourceNotFoundExceptionResolverFactory extends ExceptionResolverFactorySupport {

    public record Detail(String resourceType, String resourceProperty) {
    }

    private static ResourceNotFoundExceptionResolverFactory.Detail createDetail(Exception exception) {
        if (!(exception instanceof ResourceNotFoundException e)) {
            throw new IllegalArgumentException();
        }
        return new ResourceNotFoundExceptionResolverFactory.Detail(
                e.getResourceType(),
                e.getResourceProperty()
        );
    }

    public ResourceNotFoundExceptionResolverFactory() {
        super(ResourceNotFoundException.class, StatusTypeEnum.RESOURCE_NOT_FOUND, ResourceNotFoundExceptionResolverFactory::createDetail);
    }

}
