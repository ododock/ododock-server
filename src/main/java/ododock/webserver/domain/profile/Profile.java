package ododock.webserver.domain.profile;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Embeddable
@Getter
@Builder
@Access(AccessType.FIELD)
@NoArgsConstructor
public class Profile {

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Nullable
    @Column(name = "fullname")
    private String fullname;

    @Nullable
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Embedded
    private ProfileImage profileImage;

    @Builder
    public Profile(String nickname, String fullname, LocalDate birthDate, ProfileImage profileImage) {
        this.nickname = nickname;
        this.fullname = fullname;
        this.birthDate = birthDate;
        this.profileImage = profileImage != null ? profileImage : ProfileImage.builder().build();
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void updateFullname(final String fullname) {
        this.fullname = fullname;
    }

    public void updateProfileImage(final String sourcePath, final String filetype) {
        this.profileImage = ProfileImage.builder()
                .sourcePath(sourcePath)
                .fileType(filetype)
                .build();
    }

    public void deleteProfileImage() {
        this.profileImage.updateFileType(null);
        this.profileImage.updateSourcePath(null);
    }

}
