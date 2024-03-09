package ododock.webserver.service;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.exception.ResourceAlreadyExistsException;
import ododock.webserver.exception.ResourceNotFoundException;
import ododock.webserver.repository.ProfileRepository;
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
    public ProfileDetailsResponse getProfile(final Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(Profile.class, profileId));
        return ProfileDetailsResponse.of(profile);
    }

    @Transactional
    public void createProfile(final Profile profile) {
        if (validateNickname(profile.getNickname())) {
            throw new ResourceAlreadyExistsException(Profile.class, profile.getNickname());
        }
        profileRepository.save(profile);
    }

    @Transactional
    public void updateProfile(final Long profileId, final ProfileUpdate request) {
        if (validateNickname(request.nickname())) {
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

    @Transactional(readOnly = true)
    public boolean validateNickname(final String nickname) {
        return profileRepository.existsByNickname(nickname);
    }

}
