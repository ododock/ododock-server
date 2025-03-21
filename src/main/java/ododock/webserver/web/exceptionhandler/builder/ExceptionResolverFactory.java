package ododock.webserver.web.exceptionhandler.builder;

import ododock.webserver.web.exceptionhandler.log.LogLevel;
import ododock.webserver.web.exceptionhandler.log.LogLevelResolver;
import ododock.webserver.web.exceptionhandler.response.ExceptionResponse;
import ododock.webserver.web.exceptionhandler.response.ExceptionResponseResolver;
import org.springframework.core.Ordered;

public abstract class ExceptionResolverFactory {

    public ExceptionResponseResolver createExceptionResponseResolver() {
        return new ExceptionResponseResolver() {

            @Override
            public ExceptionResponse resolve(Exception exception) {
                return resolveExceptionResponse(exception);
            }

            @Override
            public boolean supports(Class<? extends Exception> exceptionClass) {
                return supportsInternal(exceptionClass);
            }

            @Override
            public int getOrder() {
                return getOrderInternal();
            }
        };
    }

    public LogLevelResolver createLogLevelResolver() {
        return new LogLevelResolver() {

            @Override
            public LogLevel resolve(Exception exception) {
                return resolveLogLevel(exception);
            }

            @Override
            public boolean supports(Class<? extends Exception> exceptionClass) {
                return supportsInternal(exceptionClass);
            }

            @Override
            public int getOrder() {
                return getOrderInternal();
            }
        };
    }

    protected int getOrderInternal() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    protected abstract boolean supportsInternal(Class<? extends Exception> exceptionClass);

    protected abstract ExceptionResponse resolveExceptionResponse(Exception exception);

    protected abstract LogLevel resolveLogLevel(Exception exception);

}
