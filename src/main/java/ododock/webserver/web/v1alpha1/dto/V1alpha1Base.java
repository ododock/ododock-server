package ododock.webserver.web.v1alpha1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class V1alpha1Base extends V1alpha1BaseTime {

    public V1alpha1Base(@Nullable LocalDateTime createdAt, @Nullable LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
    }

}
