package ododock.webserver.web.exceptionhandler.factories;

import com.mongodb.lang.Nullable;
import io.netty.handler.timeout.TimeoutException;
import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactory;
import ododock.webserver.web.exceptionhandler.log.LogLevel;
import ododock.webserver.web.exceptionhandler.response.ExceptionResponse;
import ododock.webserver.web.exceptionhandler.response.Status;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SpringWebFluxExceptionResolverFactory extends ExceptionResolverFactory {

    private final static Map<Class<? extends Exception>, StatusTypeEnum> STATUS_TYPE_ENUM_MAP;

    static {
        STATUS_TYPE_ENUM_MAP = new HashMap<>();
        STATUS_TYPE_ENUM_MAP.put(HandlerMethodValidationException.class, StatusTypeEnum.METHOD_NOT_ALLOWED);
        STATUS_TYPE_ENUM_MAP.put(MethodNotAllowedException.class, StatusTypeEnum.METHOD_NOT_ALLOWED);
        STATUS_TYPE_ENUM_MAP.put(MissingRequestValueException.class, StatusTypeEnum.METHOD_NOT_ALLOWED);
        STATUS_TYPE_ENUM_MAP.put(NotAcceptableStatusException.class, StatusTypeEnum.METHOD_NOT_ALLOWED);
        STATUS_TYPE_ENUM_MAP.put(UnsupportedMediaTypeStatusException.class, StatusTypeEnum.METHOD_NOT_ALLOWED);
        STATUS_TYPE_ENUM_MAP.put(UnsatisfiedRequestParameterException.class, StatusTypeEnum.METHOD_NOT_ALLOWED);
        STATUS_TYPE_ENUM_MAP.put(WebExchangeBindException.class, StatusTypeEnum.METHOD_NOT_ALLOWED);
        STATUS_TYPE_ENUM_MAP.put(MissingPathVariableException.class, StatusTypeEnum.INTERNAL_SERVER_ERROR);
        STATUS_TYPE_ENUM_MAP.put(TimeoutException.class, StatusTypeEnum.REQUEST_TIMEOUT);
        STATUS_TYPE_ENUM_MAP.put(IllegalStateException.class, StatusTypeEnum.INTERNAL_SERVER_ERROR);
    }

    private static Optional<StatusTypeEnum> findByExceptionClass(Class<? extends Exception> exceptionClass) {
        return Optional.ofNullable(STATUS_TYPE_ENUM_MAP.get(exceptionClass));
    }

    @Nullable
    private static Object createDetail(Exception e) {
        if (WebExchangeBindException.class.isAssignableFrom(e.getClass())) {
            return ((WebExchangeBindException) e).getReason();
        } else {
            return null;
        }
    }

    @Override
    protected boolean supportsInternal(Class<? extends Exception> exceptionClass) {
        return findByExceptionClass(exceptionClass).isPresent();
    }

    @Override
    protected ExceptionResponse resolveExceptionResponse(Exception exception) {
        StatusTypeEnum statusTypeEnum = findByExceptionClass(exception.getClass()).get();
        Status status = new Status(statusTypeEnum.getType(), createDetail(exception));
        return new ExceptionResponse(statusTypeEnum.getHttpStatus(), status);
    }

    @Override
    protected LogLevel resolveLogLevel(Exception exception) {
        StatusTypeEnum statusTypeEnum = findByExceptionClass(exception.getClass()).get();
        return statusTypeEnum.getLogLevel();
    }
}