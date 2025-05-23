package ododock.webserver.web.v1alpha1.dto.content;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ododock.webserver.web.v1alpha1.dto.V1alpha1Base;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class V1alpha1Content extends V1alpha1Base {

    @NotBlank
    private String name;
    @NotNull
    private LocalDate publishedDate;
    @Nullable
    private String isbn;
    @Nullable
    private String director;

}
