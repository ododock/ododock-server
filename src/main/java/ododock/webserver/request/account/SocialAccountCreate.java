package ododock.webserver.request.account;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder(toBuilder = true)
public record SocialAccountCreate (
        @NotBlank
        String provider,
        @NotBlank
        String providerId,
        @NotBlank
        String email,
        @Nullable
        String fullname,
        @Nullable
        @JsonDeserialize(using = LocalDateDeserializer.class)
        LocalDate birthDate,
        @Nullable
        String nickname,
        @Nullable
        Map<String, List<String>> attributes,
        @Nullable
        String imageSource,
        @Nullable
        String fileType
) {
        public SocialAccountCreate {
                if (nickname == null) {
                        nickname = UUID.randomUUID().toString().split("-")[0];
                }
        }
}
