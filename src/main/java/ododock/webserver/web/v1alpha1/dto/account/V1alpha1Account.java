package ododock.webserver.web.v1alpha1.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.SocialAccount;
import ododock.webserver.domain.profile.ProfileImage;
import ododock.webserver.web.v1alpha1.dto.V1alpha1Base;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class V1alpha1Account extends V1alpha1Base {

    @Nullable
    Long id;
    @Nullable
    String email;
    @Nullable
    String password;
    @Nullable
    String nickname;
    @Nullable
    String fullname;
    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate birthDate;
    @Nullable
    Map<String, List<String>> attributes;
    @Nullable
    String profileImageSource;
    @Nullable
    String profileImageFileType;
    @Nullable
    Set<String> providers;
    @Nullable
    Boolean emailVerified;
    @Nullable
    String verificationCode;

    public Account toDomainDto() {
        return Account.builder()
                .id(this.id)
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .fullname(this.fullname)
                .birthDate(this.birthDate)
                .attributes(this.attributes)
                .profileImage(ProfileImage.builder()
                        .sourcePath(profileImageSource)
                        .fileType(profileImageFileType)
                        .build())
                .build();
    }

    public static V1alpha1Account toControllerDto(Account domainDto) {
        return V1alpha1Account.builder()
                .id(domainDto.getId())
                .password("[REDACTED]")
                .email(domainDto.getEmail())
                .fullname(domainDto.getOwnProfile().getFullname())
                .nickname(domainDto.getOwnProfile().getNickname())
                .birthDate(domainDto.getOwnProfile().getBirthDate())
                .createdAt(domainDto.getCreatedDate())
                .updatedAt(domainDto.getLastModifiedAt())
                .providers(domainDto.getSocialAccounts().stream()
                        .map(SocialAccount::getProvider)
                        .collect(Collectors.toSet()))
                .emailVerified(domainDto.getEmailVerified())
                .build();
    }

}
