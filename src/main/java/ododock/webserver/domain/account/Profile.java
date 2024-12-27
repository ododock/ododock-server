package ododock.webserver.domain.account;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
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

    @Nullable
    @Embedded
    private ProfileImage profileImage;

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void updateFullname(final String fullname) {
        this.fullname = fullname;
    }

    public void updateProfileImage(final String imageSource, final String filetype) {
        this.profileImage = ProfileImage.builder()
                .imageSource(imageSource)
                .fileType(filetype)
                .build();
    }

}
