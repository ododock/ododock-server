package ododock.webserver.exception;

import org.apache.coyote.BadRequestException;

public class VerificationCodeExpiredException extends BadRequestException {
    public VerificationCodeExpiredException(String message) {
        super(message);
    }
}
