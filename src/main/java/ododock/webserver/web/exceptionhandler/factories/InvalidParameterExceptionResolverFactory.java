package ododock.webserver.web.exceptionhandler.factories;

import ododock.webserver.web.InvalidParameterException;
import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactorySupport;

public class InvalidParameterExceptionResolverFactory extends ExceptionResolverFactorySupport {

    public record Detail(String parameterName, String errorMessage) {
    }

    private static InvalidParameterExceptionResolverFactory.Detail createDetail(Exception exception) {
        if (!(exception instanceof InvalidParameterException e)) {
            throw new IllegalArgumentException();
        }
        InvalidParameterException ex = (InvalidParameterException) exception;
        return new InvalidParameterExceptionResolverFactory.Detail(
                ex.getParameterName(),
                e.getErrorMessage()
        );
    }

    public InvalidParameterExceptionResolverFactory() {
        super(InvalidParameterException.class, StatusTypeEnum.ILLEGAL_PROPERTY, InvalidParameterExceptionResolverFactory::createDetail);
    }

}
