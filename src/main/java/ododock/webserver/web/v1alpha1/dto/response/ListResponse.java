package ododock.webserver.web.v1alpha1.dto.response;

import org.springframework.lang.Nullable;

import java.util.List;

public record ListResponse<T>(
        @Nullable Long ownerId,
        @Nullable Integer totalSize,
        List<T> contents
) {

    public static <T> ListResponse<T> of(@Nullable final Long ownerId, @Nullable final Integer totalSize, final List<T> content) {
        return new ListResponse<>(ownerId, totalSize, content);
    }
}
