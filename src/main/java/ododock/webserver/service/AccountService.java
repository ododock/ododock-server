package ododock.webserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.Role;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.domain.profile.ProfileImage;
import ododock.webserver.exception.ResourceAlreadyExistsException;
import ododock.webserver.exception.ResourceNotFoundException;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.repository.OAuth2AccountRepository;
import ododock.webserver.repository.ProfileRepository;
import ododock.webserver.request.AccountCreate;
import ododock.webserver.request.AccountPasswordUpdate;
import ododock.webserver.response.AccountCreateResponse;
import ododock.webserver.response.AccountDetailsResponse;
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
    private final OAuth2AccountRepository oAuth2AccountRepository;
    private final ProfileRepository profileRepository;
    private final ProfileService profileService;

    @Transactional(readOnly = true)
    public boolean isAvailableEmail(final String email) {
        return !accountRepository.existsByEmail(email) && !oAuth2AccountRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public AccountDetailsResponse getAccount(final Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        return AccountDetailsResponse.of(account);
    }

    @Transactional
    public AccountCreateResponse createAccount(final AccountCreate request) {
        if (!isAvailableEmail(request.email())) {
            throw new ResourceAlreadyExistsException(Account.class, request.email());
        }
        if (profileRepository.existsByNickname(request.nickname())) {
            throw new ResourceAlreadyExistsException(Profile.class, request.nickname());
        }
        Account newAccount = Account.builder()
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .birthDate(request.birthDate())
                .fullname(request.fullname())
                .roles(Set.of(Role.USER))
                .nickname(request.nickname())
                .profileImage(ProfileImage.builder()
                        .imageSource(request.imageSource())
                        .fileType(request.fileType())
                        .build())
                .build();
        accountRepository.save(newAccount);
        return AccountCreateResponse.builder()
                .accountId(newAccount.getId())
                .profileId(newAccount.getOwnProfile().getId())
                .build();
    }

    @Transactional
    public void updateAccountPassword(final Long accountId, final AccountPasswordUpdate request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        account.updatePassword(passwordEncoder.encode(request.password()));
    }

    @Transactional
    public void deleteAccount(final Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        accountRepository.delete(account);
    }

}
