package ododock.webserver.exception;

public record FieldErrorResponse (
    String field,
    Object value,
    String reason
) {
}
