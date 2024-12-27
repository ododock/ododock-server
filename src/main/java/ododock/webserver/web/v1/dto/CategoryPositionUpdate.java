package ododock.webserver.web.v1.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record CategoryPositionUpdate(
        @NotNull
        @PositiveOrZero
        Integer newPosition
) {
}
