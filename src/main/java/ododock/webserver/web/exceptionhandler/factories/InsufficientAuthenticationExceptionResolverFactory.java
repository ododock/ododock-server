package ododock.webserver.web.exceptionhandler.factories;

import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactorySupport;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.InsufficientAuthenticationException;

public class InsufficientAuthenticationExceptionResolverFactory extends ExceptionResolverFactorySupport {

    public InsufficientAuthenticationExceptionResolverFactory() {
        super(InsufficientAuthenticationException.class, StatusTypeEnum.INSUFFICIENT_AUTHENTICATION,e -> null);
    }

    @Override
    protected int getOrderInternal() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }

}
