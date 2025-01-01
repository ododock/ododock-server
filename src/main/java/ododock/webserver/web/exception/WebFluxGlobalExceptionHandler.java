package ododock.webserver.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class WebFluxGlobalExceptionHandler {

    @ExceptionHandler(AbstractDomainException.class)
    @ResponseBody
    public Mono<ProblemDetail> handleDomainException(AbstractDomainException ex, ServerWebExchange exchange) {
        ErrorCode errorCode = ex.getErrorCode();
        exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(errorCode.getStatus()));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(errorCode.getStatus()),
                errorCode.getMessage()
        );
        problemDetail.setProperty("errorCode", errorCode.getCode());

        return Mono.just(problemDetail);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseBody
    public Mono<ProblemDetail> handleValidationException(WebExchangeBindException ex, ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation error"
        );
        problemDetail.setProperty("fields", ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldErrorResponse(
                        fieldError.getField(),
                        fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage()
                )).toList());

        return Mono.just(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Mono<ProblemDetail> handleUnexpectedException(Exception ex, ServerWebExchange exchange) {
        log.error("Unexpected error occurred", ex);
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error Occurred"
        );

        return Mono.just(problemDetail);
    }
}
