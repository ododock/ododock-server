package ododock.webserver.response;

import org.springframework.lang.Nullable;

import java.util.List;

public record ListResponse<T>(
        @Nullable Long ownerId,
        List<T> content
) {

    public static <T> ListResponse<T> of(@Nullable final Long ownerId, final List<T> content) {
        return new ListResponse<>(ownerId, content);
    }
}
