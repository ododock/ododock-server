package ododock.webserver.web.exceptionhandler;

import lombok.NonNull;
import ododock.webserver.web.WebRequestLogMessageFactory;
import ododock.webserver.web.exceptionhandler.log.LogLevel;
import ododock.webserver.web.exceptionhandler.log.LogLevelResolver;
import ododock.webserver.web.exceptionhandler.log.Logger;
import ododock.webserver.web.exceptionhandler.log.LoggerResolver;
import ododock.webserver.web.exceptionhandler.response.ExceptionResponse;
import ododock.webserver.web.exceptionhandler.response.ExceptionResponseResolver;
import ododock.webserver.web.exceptionhandler.response.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.function.Function;

public class WebRequestExceptionHandler {

    @NonNull
    private final ExceptionResponseResolver exceptionResponseResolver;

    private final Function<Status, Object> bodyConverter;

    private final LogLevelResolver logLevelResolver;

    private final LoggerResolver loggerResolver;

    private final WebRequestLogMessageFactory logMessageFactory;

    public WebRequestExceptionHandler(
            ExceptionResponseResolver exceptionResponseResolver,
            Function<Status, Object> bodyConverter,
            LogLevelResolver logLevelResolver) {
        this.exceptionResponseResolver = exceptionResponseResolver;
        this.bodyConverter = bodyConverter; // StatusConverter의 convert 메서드를 받음
        this.logLevelResolver = logLevelResolver;
        this.loggerResolver = new LoggerResolver(WebRequestExceptionHandler.class);
        this.logMessageFactory = new WebRequestLogMessageFactory();
    }

    public ResponseEntity<Object> handle(WebRequest request, Exception e) {
        if (!supports(e.getClass())) {
            throw new IllegalArgumentException();
        }

        ResponseEntity<Object> responseEntity = createResponse(e);
        log(request, e);

        return responseEntity;
    }

    private ResponseEntity<Object> createResponse(Exception e) {
        ExceptionResponse exceptionResponse = this.exceptionResponseResolver.resolve(e);
        Object body = this.bodyConverter.apply(exceptionResponse.body());

        return new ResponseEntity<>(body, exceptionResponse.httpStatus());
    }

    private void log(WebRequest request, Exception e) {
        LogLevel logLevel = this.logLevelResolver.resolve(e);
        Logger logger = this.loggerResolver.resolve(logLevel);
        String message = this.logMessageFactory.createMessage(request, e, true);
        logger.log(message);
    }

    private boolean supports(Class<? extends Exception> exceptionClass) {
        return this.exceptionResponseResolver.supports(exceptionClass) && this.logLevelResolver.supports(exceptionClass);
    }

}
