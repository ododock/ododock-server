package ododock.webserver.exception;

public class InvalidAccountPropertyException extends AbstractDomainException {
    public InvalidAccountPropertyException(String message) {
        super(ErrorCode.INVALID_ACCOUNT_INFO, message);
    }
}
