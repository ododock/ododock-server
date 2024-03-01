package ododock.webserver.response;

import java.util.List;

public record ListResponse<T>(
        List<T> content
) {

    public static <T> ListResponse<T> of(final List<T> content) {
        return new ListResponse<>(content);
    }
}
