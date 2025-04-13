package ododock.webserver.domain.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.profile.ProfileImage;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.web.ResourceConflictException;
import ododock.webserver.web.ResourceNotFoundException;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Account;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
        Assert.notNull(account.getEmail(), "email must not be null");
        Assert.notNull(account.getPassword(), "password must not be null");
        Assert.notNull(account.getOwnProfile().getNickname(), "nickname must not be null");
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
                .build();
        accountRepository.save(newAccount);
    }

    @Transactional
    public void updateAccountPassword(final Long accountId, final V1alpha1Account request) {
        Assert.notNull(request.getPassword(), "password must not be null");
        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        account.updatePassword(passwordEncoder.encode(request.getPassword()));
    }

    @Transactional
    public void deleteAccount(final Long accountId) {
        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        accountRepository.delete(account);
    }

}
