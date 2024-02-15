package ododock.webserver.domain.profile;

import jakarta.persistence.Embeddable;
import org.springframework.lang.Nullable;

@Embeddable
public class ProfileImage {

    private String imageSource;

    private String fileType;

}
