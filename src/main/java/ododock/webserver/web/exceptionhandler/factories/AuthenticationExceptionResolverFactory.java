package ododock.webserver.web.exceptionhandler.factories;

import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactorySupport;
import org.springframework.security.core.AuthenticationException;

public class AuthenticationExceptionResolverFactory extends ExceptionResolverFactorySupport {

    public AuthenticationExceptionResolverFactory() {
        super(AuthenticationException.class, StatusTypeEnum.UNAUTHORIZED, e -> null);
    }

}
