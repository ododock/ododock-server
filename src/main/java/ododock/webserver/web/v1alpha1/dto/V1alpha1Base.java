package ododock.webserver.web.v1alpha1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class V1alpha1Base extends V1alpha1BaseTime {

    public V1alpha1Base(@Nullable Instant createdAt, @Nullable Instant updatedAt) {
        super(createdAt, updatedAt);
    }

}
