package ododock.webserver.response;

import lombok.Builder;

public record ValidateResponse(
        Boolean availability
) {
    public static ValidateResponse of(final boolean avilability) {
        return new ValidateResponse(avilability);
    }
}
