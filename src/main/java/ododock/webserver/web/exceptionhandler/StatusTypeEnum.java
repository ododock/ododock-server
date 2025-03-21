package ododock.webserver.web.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ododock.webserver.web.exceptionhandler.log.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * exception 타입에 대한 정보(예외 타입, HTTP 상태 코드, 로그 레벨)를 정의한 enum
 */
@Getter
@AllArgsConstructor
public enum StatusTypeEnum {

    // 400...

    ILLEGAL_PROPERTY("ILLEGAL_PROPERTY", HttpStatus.BAD_REQUEST, LogLevel.DEBUG),
    MISSING_PATH_VARIABLE("MISSING_PATH_VARIABLE", HttpStatus.BAD_REQUEST, LogLevel.DEBUG),
    MISSING_REQUEST_PARAMETER("MISSING_REQUEST_PARAMETER", HttpStatus.BAD_REQUEST, LogLevel.DEBUG),
    HTTP_MESSAGE_READ_ERROR("HTTP_MESSAGE_READ_ERROR", HttpStatus.BAD_REQUEST, LogLevel.DEBUG),
    INVALID_PARAMETER("INVALID_PARAMETER", HttpStatus.BAD_REQUEST, LogLevel.DEBUG),
    INVALID_MULTIPART_REQUEST("INVALID_MULTIPART_REQUEST", HttpStatus.BAD_REQUEST, LogLevel.DEBUG),
    BIND_ERROR("BIND_ERROR", HttpStatus.BAD_REQUEST, LogLevel.DEBUG),
    ARGUMENT_TYPE_MISMATCH("ARGUMENT_TYPE_MISMATCH", HttpStatus.BAD_REQUEST, LogLevel.DEBUG),
    INVALID_VERIFICATION("INVALID_VERIFICATION", HttpStatus.BAD_REQUEST, LogLevel.DEBUG),

    // 401...

    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED, LogLevel.DEBUG),
    BAD_CREDENTIAL("BAD_CREDENTIAL", HttpStatus.UNAUTHORIZED, LogLevel.DEBUG),
    INSUFFICIENT_AUTHENTICATION("INSUFFICIENT_AUTHENTICATION", HttpStatus.UNAUTHORIZED, LogLevel.DEBUG),
    OAUTH2_ERROR("OAUTH2_ERROR", HttpStatus.UNAUTHORIZED, LogLevel.DEBUG),

    // 403...

    FORBIDDEN("FORBIDDEN", HttpStatus.FORBIDDEN, LogLevel.DEBUG),

    // 404...

    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND, LogLevel.DEBUG),
    NO_HANDLER_FOUND("NO_HANDLER_FOUND", HttpStatus.NOT_FOUND, LogLevel.DEBUG),

    // 405...

    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED, LogLevel.DEBUG),

    // 406...

    NOT_ACCEPTABLE("NOT_ACCEPTABLE", HttpStatus.NOT_ACCEPTABLE, LogLevel.DEBUG),

    // 409...

    CONFLICT("CONFLICT", HttpStatus.CONFLICT, LogLevel.DEBUG),

    // 415...

    UNSUPPORTED_MEDIA_TYPE("UNSUPPORTED_MEDIA_TYPE", HttpStatus.UNSUPPORTED_MEDIA_TYPE, LogLevel.DEBUG),

    // 429...

    TOO_MANY_REQUESTS("TOO_MANY_REQUESTS", HttpStatus.TOO_MANY_REQUESTS, LogLevel.DEBUG),

    // 500...

    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR),
    REQUEST_BINDING_ERROR("REQUEST_BINDING_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR),
    CONVERSION_NOT_SUPPORTED("CONVERSION_NOT_SUPPORTED", HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR),
    TYPE_MISMATCH_ERROR("TYPE_MISMATCH_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR),
    HTTP_MESSAGE_WRITE_ERROR("HTTP_MESSAGE_WRITE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR),

    // 501...

    NOT_IMPLEMENTED("NOT_IMPLEMENTED", HttpStatus.NOT_IMPLEMENTED, LogLevel.DEBUG),

    // 503...

    REQUEST_TIMEOUT("REQUEST_TIMEOUT", HttpStatus.SERVICE_UNAVAILABLE, LogLevel.ERROR);

    /**
     * Exception 타입
     */
    @NonNull
    private final String type;
    /**
     * HTTP 상태
     */
    @NonNull
    private final HttpStatus httpStatus;
    /**
     * 로그 레벨
     */
    @NonNull
    private final LogLevel logLevel;


}
