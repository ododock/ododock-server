package ododock.webserver.web;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class VerificationCodeException extends RuntimeException {

    public VerificationCodeException(String message) {
        super(message);
    }

}
