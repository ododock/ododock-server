package ododock.webserver.domain.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.web.exception.ResourceAlreadyExistsException;
import ododock.webserver.web.exception.ResourceNotFoundException;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.web.v1alpha1.dto.account.AccountCreate;
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
    public void createDaoAccount(final AccountCreate request) throws Exception {
        if (!isAvailableEmail(request.email())) {
            throw new ResourceAlreadyExistsException(Account.class, request.email());
        }
        if (accountRepository.existsByOwnProfile_Nickname(request.nickname())) {
            throw new ResourceAlreadyExistsException(Account.class, request.nickname());
        }
        final Account newAccount = Account.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .birthDate(request.birthDate())
                .fullname(request.fullname())
                .roles(Set.of(Role.USER))
                .attributes(request.attributes())
                .profileImage(ProfileImage.builder()
                        .imageSource(request.profileImageSource())
                        .fileType(request.profileImageFileType())
                        .build())
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
