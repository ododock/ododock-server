package ododock.webserver.domain.account;

import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Account;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Profile;

public interface ProfileService {

    void updateProfile(V1alpha1Profile profile);

    void getProfileImage(V1alpha1Account account);

    void saveProfileImage(V1alpha1Profile profile);

    void updateProfileImage(V1alpha1Profile profile);

    void deleteProfileImage(V1alpha1Account account);

}
