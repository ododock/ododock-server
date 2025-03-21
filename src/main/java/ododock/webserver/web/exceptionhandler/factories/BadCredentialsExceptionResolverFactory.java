package ododock.webserver.web.exceptionhandler.factories;

import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactorySupport;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.BadCredentialsException;

public class BadCredentialsExceptionResolverFactory extends ExceptionResolverFactorySupport {

    public BadCredentialsExceptionResolverFactory() {
        super(BadCredentialsException.class, StatusTypeEnum.BAD_CREDENTIAL, e -> null);
    }

    @Override
    protected int getOrderInternal() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }

}
