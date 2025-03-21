package ododock.webserver.web.exceptionhandler.builder;

import lombok.AllArgsConstructor;
import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.log.LogLevel;
import ododock.webserver.web.exceptionhandler.response.ExceptionResponse;
import ododock.webserver.web.exceptionhandler.response.Status;

import java.util.function.Function;

@AllArgsConstructor
public class ExceptionResolverFactorySupport extends ExceptionResolverFactory {

    private final Class<? extends Exception> exceptionClass;
    private final StatusTypeEnum statusTypeEnum;
    private final Function<Exception, Object> detailFactory;

    @Override
    protected boolean supportsInternal(Class<? extends Exception> exceptionClass) {
        return this.exceptionClass.isAssignableFrom(exceptionClass);
    }

    @Override
    protected ExceptionResponse resolveExceptionResponse(Exception exception) {
        Status body = new Status(this.statusTypeEnum.getType(), this.detailFactory.apply(exception));
        return new ExceptionResponse(this.statusTypeEnum.getHttpStatus(), body);
    }

    @Override
    protected LogLevel resolveLogLevel(Exception exception) {
        return this.statusTypeEnum.getLogLevel();
    }

}
