package ododock.webserver.domain.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@Embeddable
@NoArgsConstructor
public class ProfileImage {

    @Nullable
    @Column(name = "image_source")
    private String imageSource;

    @Nullable
    @Column(name = "file_type")
    private String fileType;

    @Builder
    public ProfileImage(final String imageSource, final String fileType) {
        this.imageSource = imageSource;
        this.fileType = fileType;
    }

    public void updateImageSource(final String imageSource) {
        this.imageSource = imageSource;
    }

    public void updateFileType(final String fileType) {
        this.fileType = fileType;
    }

}
