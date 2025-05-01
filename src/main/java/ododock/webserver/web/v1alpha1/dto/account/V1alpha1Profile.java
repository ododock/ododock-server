package ododock.webserver.web.v1alpha1.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.domain.profile.ProfileImage;
import ododock.webserver.web.v1alpha1.dto.V1alpha1Base;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class V1alpha1Profile extends V1alpha1Base {

    @Nullable
    private String nickname;
    @Nullable
    private String fullname;
    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private V1alpha1ProfileImage profileImage;

    public Profile toDomainDto() {
        return Profile.builder()
                .nickname(this.nickname)
                .fullname(this.fullname)
                .birthDate(this.birthDate)
                .profileImage(profileImage == null ? ProfileImage.builder().build() : profileImage.toDomainDto())
                .build();
    }

    public static V1alpha1Profile toControllerDto(Profile domainDto) {
        return V1alpha1Profile.builder()
                .nickname(domainDto.getNickname())
                .fullname(domainDto.getFullname())
                .birthDate(domainDto.getBirthDate())
                .profileImage(V1alpha1ProfileImage.toControllerDto(domainDto.getProfileImage()))
                .build();
    }

}
