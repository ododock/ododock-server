package ododock.webserver.domain.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.web.ResourceConflictException;
import ododock.webserver.web.ResourceNotFoundException;
import ododock.webserver.web.v1alpha1.dto.account.AccountPasswordUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public boolean isAvailableEmail(final String email) {
        return !accountRepository.existsByEmail(email) && !accountRepository.existsBySocialAccounts_Email(email);
    }

    @Transactional(readOnly = true)
    public boolean isAvailableNickname(final String nickname) {
        return !accountRepository.existsByOwnProfile_Nickname(nickname);
    }

    @Transactional
    public void createDaoAccount(final Account account) throws Exception {
        if (!isAvailableEmail(account.getEmail())) {
            throw new ResourceConflictException(Account.class, account.getEmail());
        }
        if (accountRepository.existsByOwnProfile_Nickname(account.getOwnProfile().getNickname())) {
            throw new ResourceConflictException(Account.class, account.getOwnProfile().getNickname());
        }
        final Account newAccount = Account.builder()
                .email(account.getEmail())
                .password(passwordEncoder.encode(account.getPassword()))
                .nickname(account.getOwnProfile().getNickname())
                .birthDate(account.getOwnProfile().getBirthDate())
                .fullname(account.getOwnProfile().getFullname())
                .roles(Set.of(Role.USER))
                .attributes(account.getAttributes())
                .profileImage(account.getOwnProfile() == null
                        ? ProfileImage.builder().build()
                        : ProfileImage.builder()
                        .imageSource(account.getOwnProfile().getProfileImage().getImageSource())
                        .fileType(account.getOwnProfile().getProfileImage().getFileType())
                        .build()
                )
                .build();
        accountRepository.save(newAccount);
    }

    @Transactional
    public void updateAccountPassword(final Long accountId, final AccountPasswordUpdate request) {
        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        account.updatePassword(passwordEncoder.encode(request.password()));
    }

    @Transactional
    public void deleteAccount(final Long accountId) {
        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        accountRepository.delete(account);
    }

}
