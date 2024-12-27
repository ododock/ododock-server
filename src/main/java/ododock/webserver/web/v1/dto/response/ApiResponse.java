package ododock.webserver.web.v1.dto.response;

public record ApiResponse(
        String type,
        Long value
) {
    public static ApiResponse of(final String type, Long value) {
        return new ApiResponse(type, value);
    }
}
