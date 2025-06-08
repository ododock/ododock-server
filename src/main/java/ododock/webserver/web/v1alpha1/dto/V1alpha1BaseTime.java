package ododock.webserver.web.v1alpha1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class V1alpha1BaseTime {

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createdAt;
    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant updatedAt;

    public V1alpha1BaseTime(@Nullable Instant createdAt, @Nullable Instant updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
