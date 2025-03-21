package ododock.webserver.web.exceptionhandler.response;

import ododock.webserver.web.exceptionhandler.StatusTypeEnum;
import org.springframework.http.HttpStatus;

public class DefaultExceptionResponseResolver implements ExceptionResponseResolver {

    @Override
    public ExceptionResponse resolve(Exception exception) {
        Status status = new Status(StatusTypeEnum.INTERNAL_SERVER_ERROR.getType(), null);
        return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, status);
    }

    @Override
    public boolean supports(Class<? extends Exception> exceptionClass) {
        return true;
    }

}
