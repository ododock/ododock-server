package ododock.webserver.exception;

import org.apache.coyote.BadRequestException;

public class InvalidVerificationCodeException extends BadRequestException {
    public InvalidVerificationCodeException(String message) {
        super(message);
    }
}
