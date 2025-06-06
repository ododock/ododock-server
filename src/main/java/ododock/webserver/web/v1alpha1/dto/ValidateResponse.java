package ododock.webserver.web.v1alpha1.dto;

public record ValidateResponse(
        Boolean availability
) {
    public static ValidateResponse of(final boolean availability) {
        return new ValidateResponse(availability);
    }
}
