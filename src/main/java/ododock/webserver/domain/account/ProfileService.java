package ododock.webserver.domain.account;

import lombok.RequiredArgsConstructor;
import ododock.webserver.web.exception.ResourceAlreadyExistsException;
import ododock.webserver.web.exception.ResourceNotFoundException;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.web.v1.dto.ProfileUpdate;
import ododock.webserver.web.v1.dto.response.ProfileDetailsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public boolean isAvailableNickname(final String nickname) {
        return !accountRepository.existsByOwnProfile_Nickname(nickname);
    }

    @Transactional(readOnly = true)
    public ProfileDetailsResponse getProfile(final Long accountId) {
        Account foundAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        return ProfileDetailsResponse.of(foundAccount.getId(), foundAccount.getOwnProfile());
    }

    @Transactional
    public void updateProfile(final Long accountId, final ProfileUpdate request) {
        if (isAvailableNickname(request.nickname())) {
            throw new ResourceAlreadyExistsException(Profile.class, request.nickname());
        }
        Account ownerAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        ownerAccount.getOwnProfile().updateNickname(request.nickname());
        if (request.profileImageSource() != null && request.profileImageFileType() != null) {
            ownerAccount.getOwnProfile().updateProfileImage(request.profileImageSource(), request.profileImageFileType());
        }
    }

}