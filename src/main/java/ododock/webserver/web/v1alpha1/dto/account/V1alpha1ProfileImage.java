package ododock.webserver.web.v1alpha1.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ododock.webserver.domain.profile.ProfileImage;
import org.springframework.lang.Nullable;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@NoArgsConstructor
@AllArgsConstructor
public class V1alpha1ProfileImage {

    @Nullable
    private String sourcePath;
    @Nullable
    private String fileType;

    public static V1alpha1ProfileImage toControllerDto(ProfileImage domainDto) {
        if (domainDto == null) {
            return V1alpha1ProfileImage.builder().build();
        }
        return V1alpha1ProfileImage.builder()
                .sourcePath(domainDto.getSourcePath())
                .fileType(domainDto.getFileType())
                .build();
    }

    public ProfileImage toDomainDto() {
        return ProfileImage.builder()
                .sourcePath(this.sourcePath)
                .fileType(this.fileType)
                .build();
    }

}
