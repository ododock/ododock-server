package swim.webserver.exception;

import lombok.Getter;

@Getter
public abstract class AbstractDomainException  extends RuntimeException {

    private final ErrorCode errorCode;

    protected AbstractDomainException(final ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    protected AbstractDomainException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected AbstractDomainException(final ErrorCode errorCode, final String message, final Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    protected AbstractDomainException(final ErrorCode errorCode, final Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

}
