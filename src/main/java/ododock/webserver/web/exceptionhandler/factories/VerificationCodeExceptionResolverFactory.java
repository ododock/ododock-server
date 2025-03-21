package ododock.webserver.web.exceptionhandler.factories;

import ododock.webserver.web.VerificationCodeException;
import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactorySupport;

public class VerificationCodeExceptionResolverFactory extends ExceptionResolverFactorySupport {

    public record Detail(String message) {
    }

    private static VerificationCodeExceptionResolverFactory.Detail createDetail(Exception exception) {
        if (!(exception instanceof VerificationCodeException e)) {
            throw new IllegalArgumentException();
        }
        return new VerificationCodeExceptionResolverFactory.Detail(
                e.getMessage()
        );
    }

    public VerificationCodeExceptionResolverFactory() {
        super(VerificationCodeException.class, StatusTypeEnum.INVALID_VERIFICATION, VerificationCodeExceptionResolverFactory::createDetail);
    }

}
