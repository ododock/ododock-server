package ododock.webserver.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // TODO figure out what is this field for
    ;

    private final String code;

    private final String message;

    private final int status;
}
