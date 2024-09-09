package ododock.webserver.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("INT-01", "Internal Server Error", 500),
    RESOURCE_ALREADY_EXISTS("RES-01", "Resource already exists", 409),
    RESOURCE_NOT_FOUNDS("RES-02", "Resource not found", 404),
    TOKEN_MISSING_AUTHORIZATION_DATA("TKN-01", "Bearer token missing", 401),
    TOKEN_MISSING_ATTRIBUTES("TKN-02", "Missing required attributes", 400),
    TOKEN_INVALID("TKN-03", "Token as either expired or already revoked", 401),
    TOKEN_INVALID_NONCE("TKN-04", "Nonce does not match the nonce provided using token request", 401),
    INVALID_ACCOUNT_INFO("ACC-01", "Invalid account information", 400);
    ;

    private final String code;

    private final String message;

    private final int status;
}
