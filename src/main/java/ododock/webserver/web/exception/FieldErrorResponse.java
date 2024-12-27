package ododock.webserver.web.exception;

public record FieldErrorResponse (
    String field,
    Object value,
    String reason
) {
}
