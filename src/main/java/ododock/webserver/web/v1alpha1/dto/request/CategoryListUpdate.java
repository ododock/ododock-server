package ododock.webserver.web.v1alpha1.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record CategoryListUpdate(
        @NotNull
        List<CategoryUpdate> categories
) {
}
