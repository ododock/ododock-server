package ododock.webserver.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record CategoryOrderUpdate(
        @NotNull
        @PositiveOrZero
        Integer newOrder
) {
}
