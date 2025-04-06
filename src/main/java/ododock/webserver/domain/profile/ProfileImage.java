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
    @Column(name = "source_path")
    private String sourcePath;

    @Nullable
    @Column(name = "file_type")
    private String fileType;

    @Builder
    public ProfileImage(final String sourcePath, final String fileType) {
        this.sourcePath = sourcePath;
        this.fileType = fileType;
    }

    public void updateSourcePath(final String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public void updateFileType(final String fileType) {
        this.fileType = fileType;
    }

}
