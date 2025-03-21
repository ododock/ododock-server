package ododock.webserver.web.exceptionhandler.factories;

import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactorySupport;
import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedExceptionResolverFactory extends ExceptionResolverFactorySupport {

    public AccessDeniedExceptionResolverFactory() {
        super(AccessDeniedException.class, StatusTypeEnum.FORBIDDEN, (e) -> null);
    }

}
