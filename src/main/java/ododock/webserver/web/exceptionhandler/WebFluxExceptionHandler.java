package ododock.webserver.web.exceptionhandler;

import lombok.NonNull;
import ododock.webserver.web.ServerWebExchangeLogMessageFactory;
import ododock.webserver.web.exceptionhandler.log.LogLevel;
import ododock.webserver.web.exceptionhandler.log.LogLevelResolver;
import ododock.webserver.web.exceptionhandler.log.Logger;
import ododock.webserver.web.exceptionhandler.log.LoggerResolver;
import ododock.webserver.web.exceptionhandler.response.ExceptionResponse;
import ododock.webserver.web.exceptionhandler.response.ExceptionResponseResolver;
import ododock.webserver.web.exceptionhandler.response.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class WebFluxExceptionHandler {

    @NonNull
    private final ExceptionResponseResolver exceptionResponseResolver;

    private final Function<Status, Object> bodyConverter;

    private final LogLevelResolver logLevelResolver;

    private final LoggerResolver loggerResolver;

    private final ServerWebExchangeLogMessageFactory logMessageFactory;

    public WebFluxExceptionHandler(
            ExceptionResponseResolver exceptionResponseResolver,
            Function<Status, Object> bodyConverter,
            LogLevelResolver logLevelResolver) {
        this.exceptionResponseResolver = exceptionResponseResolver;
        this.bodyConverter = bodyConverter; // StatusConverter의 convert 메서드를 받음
        this.logLevelResolver = logLevelResolver;
        this.loggerResolver = new LoggerResolver(WebFluxExceptionHandler.class);
        this.logMessageFactory = new ServerWebExchangeLogMessageFactory();
    }

    public Mono<ResponseEntity<Object>> handle(ServerWebExchange exchange, Exception e) {
        if (!supports(e.getClass())) {
            throw new IllegalArgumentException();
        }

        ResponseEntity<Object> responseEntity = createResponse(e);
        log(exchange, e);

        return Mono.just(responseEntity);
    }

    private ResponseEntity<Object> createResponse(Exception e) {
        ExceptionResponse exceptionResponse = this.exceptionResponseResolver.resolve(e);
        Object body = this.bodyConverter.apply(exceptionResponse.body());

        return new ResponseEntity<>(body, exceptionResponse.httpStatus());
    }

    private void log(ServerWebExchange exchange, Exception e) {
        LogLevel logLevel = this.logLevelResolver.resolve(e);
        Logger logger = this.loggerResolver.resolve(logLevel);
        String message = this.logMessageFactory.createMessage(exchange, e, true);
        logger.log(message);
    }

    private boolean supports(Class<? extends Exception> exceptionClass) {
        return this.exceptionResponseResolver.supports(exceptionClass) && this.logLevelResolver.supports(exceptionClass);
    }

}
