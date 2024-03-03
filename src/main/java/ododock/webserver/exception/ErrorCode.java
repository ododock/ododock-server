package ododock.webserver.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    RESOURCE_ALREADY_EXISTS("RES-01", "Resource already exists", 404),
    RESOURCE_NOT_FOUNDS("RES-02", "Resource not found", 404),
    ;

    private final String code;

    private final String message;

    private final int status;
}
