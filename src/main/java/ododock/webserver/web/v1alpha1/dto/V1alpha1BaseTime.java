package ododock.webserver.web.v1alpha1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nimbusds.jose.shaded.gson.internal.bind.util.ISO8601Utils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;

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
