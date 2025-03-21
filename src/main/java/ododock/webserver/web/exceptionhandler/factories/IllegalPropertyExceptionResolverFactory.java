package ododock.webserver.web.exceptionhandler.factories;

import ododock.webserver.web.IllegalPropertyException;
import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactorySupport;

public class IllegalPropertyExceptionResolverFactory extends ExceptionResolverFactorySupport {

    public record Detail(String objectKind, String propertyPath, String propertyErrorMessage) {
    }

    private static Detail createDetail(Exception exception) {
        if (!(exception instanceof IllegalPropertyException e)) {
            throw new IllegalArgumentException();
        }
        return new Detail(
                e.getObjectClass().getSimpleName(),
                e.getPropertyPath(),
                e.getPropertyErrorMessage());
    }

    public IllegalPropertyExceptionResolverFactory() {
        super(IllegalPropertyException.class, StatusTypeEnum.ILLEGAL_PROPERTY, IllegalPropertyExceptionResolverFactory::createDetail);
    }

}