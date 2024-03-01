package ododock.webserver.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public record CategoryUpdate(
        @NotBlank
        List<CategoryDto> categories
) {
        @Getter
        public class CategoryDto {
                @NotBlank
                private Long categoryId;
                @NotBlank
                private String name;
                @NotBlank
                private boolean visibility;
        }

}
