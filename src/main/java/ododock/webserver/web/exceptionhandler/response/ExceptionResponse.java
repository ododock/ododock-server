package ododock.webserver.web.exceptionhandler.response;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(
        HttpStatus httpStatus,
        Status body
) {
}
