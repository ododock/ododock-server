package ododock.webserver.web.exceptionhandler.factories;

import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactorySupport;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class MethodArgumentTypeMismatchExceptionResolverFactory extends ExceptionResolverFactorySupport {

    public record Detail(String argumentName) {
    }

    private static Detail createDetail(Exception exception) {
        if (!(exception instanceof MethodArgumentTypeMismatchException e)) {
            throw new IllegalArgumentException();
        }
        return new Detail(e.getName());
    }

    public MethodArgumentTypeMismatchExceptionResolverFactory() {
        super(
                MethodArgumentTypeMismatchException.class,
                StatusTypeEnum.ARGUMENT_TYPE_MISMATCH,
                MethodArgumentTypeMismatchExceptionResolverFactory::createDetail);
    }

}
