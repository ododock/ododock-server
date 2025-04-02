package ododock.webserver.web.v1alpha1.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.Profile;
import ododock.webserver.domain.account.ProfileImage;
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
    private Long id;
    @Nullable
    private String nickname;
    @Nullable
    private String fullname;
    @Nullable
    private LocalDate birthDate;
    @Nullable
    private String profileImageSource;
    @Nullable
    private String profileImageFileType;

    public Profile toDomainDto() {
        return Profile.builder()
                .nickname(this.nickname)
                .fullname(this.fullname)
                .birthDate(this.birthDate)
                .profileImage(ProfileImage.builder()
                        .imageSource(this.profileImageSource)
                        .fileType(this.profileImageFileType)
                        .build())
                .build();
    }

    public static V1alpha1Profile toControllerDto(Account domainDto) {
        return V1alpha1Profile.builder()
                .id(domainDto.getId())
                .nickname(domainDto.getOwnProfile().getNickname())
                .fullname(domainDto.getOwnProfile().getFullname())
                .birthDate(domainDto.getOwnProfile().getBirthDate())
                .profileImageSource(domainDto.getOwnProfile().getProfileImage().getImageSource())
                .profileImageFileType(domainDto.getOwnProfile().getProfileImage().getFileType())
                .build();
    }

}
