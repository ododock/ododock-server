package ododock.webserver.web.exceptionhandler.factories;

import lombok.NonNull;
import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactorySupport;
import org.springframework.core.Ordered;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class OAuth2AuthenticationExceptionResolverFactory extends ExceptionResolverFactorySupport {

    public record Detail(@NonNull String errorCode, @NonNull String errorDescription, @NonNull String uri) {
    }

    private static Detail createDetail(Exception exception) {
        if (!(exception instanceof org.springframework.security.oauth2.core.OAuth2AuthenticationException e)) {
            throw new IllegalArgumentException();
        }
        return new Detail(
                e.getError().getErrorCode(),
                e.getError().getDescription(),
                e.getError().getUri());
    }

    public OAuth2AuthenticationExceptionResolverFactory() {
        super(OAuth2AuthenticationException.class, StatusTypeEnum.OAUTH2_ERROR, OAuth2AuthenticationExceptionResolverFactory::createDetail);
    }

    @Override
    protected int getOrderInternal() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }

}
