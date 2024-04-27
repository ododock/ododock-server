package ododock.webserver.service;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.domain.profile.ProfileImage;
import ododock.webserver.exception.ResourceAlreadyExistsException;
import ododock.webserver.exception.ResourceNotFoundException;
import ododock.webserver.repository.ProfileRepository;
import ododock.webserver.request.ProfileCreate;
import ododock.webserver.request.ProfileImageUpdate;
import ododock.webserver.request.ProfileUpdate;
import ododock.webserver.response.ProfileDetailsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public boolean isAvailableNickname(final String nickname) {
        return !profileRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public ProfileDetailsResponse getProfile(final Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));
        return ProfileDetailsResponse.of(profile);
    }

    @Transactional
    public void createProfile(final Account ownerAccount, final ProfileCreate request) {
        if (!isAvailableNickname(request.nickname())) {
            throw new ResourceAlreadyExistsException(Profile.class, request.nickname());
        }
        Profile newProfile = Profile.builder()
                .nickname(request.nickname())
                .profileImage(ProfileImage.builder()
                        .imageSource(request.imageSource())
                        .fileType(request.fileType())
                        .build())
                .build();
        profileRepository.save(newProfile);
    }

    @Transactional
    public void updateProfile(final Long profileId, final ProfileUpdate request) {
        if (isAvailableNickname(request.nickname())) {
            throw new ResourceAlreadyExistsException(Profile.class, request.nickname());
        }
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));
        profile.updateNickname(request.nickname());
        profile.updateProfileImage(request.imageSource(), request.fileType());
    }

    @Transactional
    public void updateProfileImage(final Long profileId, final ProfileImageUpdate request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));
        profile.updateProfileImage(request.imageSource(), request.fileType());
    }

}
