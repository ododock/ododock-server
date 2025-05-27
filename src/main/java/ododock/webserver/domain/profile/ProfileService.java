package ododock.webserver.domain.profile;

import ododock.webserver.web.v1alpha1.dto.account.ImageFile;

import java.io.IOException;
import java.util.Optional;

public interface ProfileService {

    Profile getProfile(Long accountId);

    Profile updateProfile(Long accountId, Profile profile);

    Optional<ProfileImage> getProfileImage(Long accountId);

    ProfileImage saveProfileImage(Long accountId, ImageFile file) throws IOException;

    ProfileImage updateProfileImage(Long accountId, ImageFile file);

    void removeProfileImage(Long accountId) throws IOException;

}
