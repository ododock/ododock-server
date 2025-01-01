package ododock.webserver.web.v1alpha1.dto.request;

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
