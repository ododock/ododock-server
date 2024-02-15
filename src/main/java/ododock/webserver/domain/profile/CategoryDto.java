package ododock.webserver.domain.profile;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class CategoryDto {

    @Nullable
    private Long categoryId;

    @Nullable
    private Long accountId;

    private String name;

}
