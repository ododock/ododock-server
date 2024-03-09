package ododock.webserver.domain.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Embeddable
@RequiredArgsConstructor
public class ProfileImage {

    @Column(name = "image_source")
    private String imageSource;

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
