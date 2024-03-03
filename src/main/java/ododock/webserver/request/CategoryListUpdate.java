package ododock.webserver.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record CategoryListUpdate(
        @NotNull
        List<CategoryUpdate> categories
) {
}
