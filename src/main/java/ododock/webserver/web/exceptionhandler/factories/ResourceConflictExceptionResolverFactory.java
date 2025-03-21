package ododock.webserver.web.exceptionhandler.factories;

import ododock.webserver.web.ResourceConflictException;
import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactorySupport;

public class ResourceConflictExceptionResolverFactory extends ExceptionResolverFactorySupport {

    public record Detail(String resourceType, String resourceProperty) {
    }

    private static ResourceConflictExceptionResolverFactory.Detail createDetail(Exception exception) {
        if (!(exception instanceof ResourceConflictException e)) {
            throw new IllegalArgumentException();
        }
        return new ResourceConflictExceptionResolverFactory.Detail(
                e.getResourceType(),
                e.getResourceProperty()
        );
    }

    public ResourceConflictExceptionResolverFactory() {
        super(ResourceConflictException.class, StatusTypeEnum.CONFLICT, ResourceConflictExceptionResolverFactory::createDetail);
    }

}
