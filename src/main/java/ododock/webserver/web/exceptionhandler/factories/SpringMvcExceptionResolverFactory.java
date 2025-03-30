package ododock.webserver.web.exceptionhandler.factories;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactory;
import ododock.webserver.web.exceptionhandler.log.LogLevel;
import ododock.webserver.web.exceptionhandler.response.ExceptionResponse;
import ododock.webserver.web.exceptionhandler.response.Status;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SpringMvcExceptionResolverFactory extends ExceptionResolverFactory {

    public record MissingPathVariableDetail(String VariableName) {
    }

    public record MissingServletRequestParameterDetail(@NonNull String parameterName) {
    }

    public record MethodArgumentNotValidDetail(@NotNull String parameterName) {
    }

    private final static Map<Class<? extends Exception>, StatusTypeEnum> STATUS_TYPE_ENUM_MAP;

    static {
        STATUS_TYPE_ENUM_MAP = new HashMap<>();
        STATUS_TYPE_ENUM_MAP.put(BadRequestException.class, StatusTypeEnum.BAD_REQUEST);
        STATUS_TYPE_ENUM_MAP.put(HttpRequestMethodNotSupportedException.class, StatusTypeEnum.METHOD_NOT_ALLOWED);
        STATUS_TYPE_ENUM_MAP.put(HttpMediaTypeNotSupportedException.class, StatusTypeEnum.UNSUPPORTED_MEDIA_TYPE);
        STATUS_TYPE_ENUM_MAP.put(HttpMediaTypeNotAcceptableException.class, StatusTypeEnum.NOT_ACCEPTABLE);
        STATUS_TYPE_ENUM_MAP.put(MissingPathVariableException.class, StatusTypeEnum.MISSING_PATH_VARIABLE);
        STATUS_TYPE_ENUM_MAP.put(MissingServletRequestParameterException.class, StatusTypeEnum.MISSING_REQUEST_PARAMETER);
        STATUS_TYPE_ENUM_MAP.put(ServletRequestBindingException.class, StatusTypeEnum.REQUEST_BINDING_ERROR);
        STATUS_TYPE_ENUM_MAP.put(ConversionNotSupportedException.class, StatusTypeEnum.CONVERSION_NOT_SUPPORTED);
        STATUS_TYPE_ENUM_MAP.put(TypeMismatchException.class, StatusTypeEnum.TYPE_MISMATCH_ERROR);
        STATUS_TYPE_ENUM_MAP.put(HttpMessageNotReadableException.class, StatusTypeEnum.HTTP_MESSAGE_READ_ERROR);
        STATUS_TYPE_ENUM_MAP.put(HttpMessageNotWritableException.class, StatusTypeEnum.HTTP_MESSAGE_WRITE_ERROR);
        STATUS_TYPE_ENUM_MAP.put(MethodArgumentNotValidException.class, StatusTypeEnum.INVALID_PARAMETER);
        STATUS_TYPE_ENUM_MAP.put(MissingServletRequestPartException.class, StatusTypeEnum.INVALID_MULTIPART_REQUEST);
        STATUS_TYPE_ENUM_MAP.put(BindException.class, StatusTypeEnum.BIND_ERROR);
        STATUS_TYPE_ENUM_MAP.put(NoHandlerFoundException.class, StatusTypeEnum.NO_HANDLER_FOUND);
        STATUS_TYPE_ENUM_MAP.put(AsyncRequestTimeoutException.class, StatusTypeEnum.REQUEST_TIMEOUT);
    }

    private static Optional<StatusTypeEnum> findByExceptionClass(Class<? extends Exception> exceptionClass) {
        return Optional.ofNullable(STATUS_TYPE_ENUM_MAP.get(exceptionClass));
    }

    @Nullable
    private static Object createDetail(Exception e) {
        if (MissingPathVariableException.class.isAssignableFrom(e.getClass())) {
            return new MissingPathVariableDetail(((MissingPathVariableException) e).getVariableName());
        } else if (MissingServletRequestParameterException.class.isAssignableFrom(e.getClass())) {
            return new MissingServletRequestParameterDetail(((MissingServletRequestParameterException) e).getParameterName());
        } else if (MethodArgumentNotValidException.class.isAssignableFrom(e.getClass())) {
            return new MethodArgumentNotValidDetail(((MethodArgumentNotValidException) e).getParameter().getParameterName());
        } else {
            return null;
        }
    }

    /**
     * 해당 Exception Hierarchy(예외 계층)에 해당하는 Exception 정보가 매핑이 되어있는지 확인한다.
     * SpringMvcException 예외 계층에 존재 또는 등록된 Exception 정보(StatusTypeEnum)을 참조한다.
     *
     * @param exceptionClass
     * @return
     */
    @Override
    protected boolean supportsInternal(Class<? extends Exception> exceptionClass) {
        return findByExceptionClass(exceptionClass).isPresent();
    }

    /**
     * 주어진 Exception에 대한 Response를 생성한다.
     *
     * @param exception
     * @return
     */
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
