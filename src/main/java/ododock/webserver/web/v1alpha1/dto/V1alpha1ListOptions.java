package ododock.webserver.web.v1alpha1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nullable;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class V1alpha1ListOptions {

    @Positive
    @Builder.Default
    private Integer pageOffset = 0;
    @Positive
    @Builder.Default
    private Integer pageSize = 100;
    @Positive
    @Builder.Default
    private Integer size = 100;
    @Nullable
    private String sort;
    @Nullable
    private String sortKeys;

    public V1alpha1ListOptions() {
        this.pageOffset = 1;
        this.pageSize = 100;
        this.size = 100;
    }

}
