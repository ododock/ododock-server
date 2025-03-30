package ododock.webserver.domain.account;

import lombok.RequiredArgsConstructor;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.web.ResourceConflictException;
import ododock.webserver.web.ResourceNotFoundException;
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
    public Account getProfile(final Long accountId) {
        Account foundAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        return foundAccount;
    }

    @Transactional
    public void updateProfile(final Long accountId, final Profile request) {
        if (!isAvailableNickname(request.getNickname())) {
            throw new ResourceConflictException(Profile.class, request.getNickname());
        }
        Account ownerAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        ownerAccount.getOwnProfile().updateNickname(request.getNickname());
        if (request.getProfileImage().getImageSource() != null && request.getProfileImage().getFileType() != null) {
            ownerAccount.getOwnProfile()
                    .updateProfileImage(
                            request.getProfileImage().getImageSource(),
                            request.getProfileImage().getFileType())
            ;
        }
    }

}
