package ododock.webserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.Role;
import ododock.webserver.domain.account.SocialAccount;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.domain.profile.ProfileImage;
import ododock.webserver.exception.ResourceAlreadyExistsException;
import ododock.webserver.exception.ResourceNotFoundException;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.repository.ProfileRepository;
import ododock.webserver.request.AccountCreate;
import ododock.webserver.request.AccountPasswordUpdate;
import ododock.webserver.request.CompleteAccountRegister;
import ododock.webserver.request.OAuthAccountConnect;
import ododock.webserver.response.AccountCreateResponse;
import ododock.webserver.response.AccountDetailsResponse;
import ododock.webserver.security.response.OAuth2UserInfo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public boolean isAvailableEmail(final String email) {
        return !accountRepository.existsByEmail(email) && !accountRepository.existsBySocialAccountsEmail(email);
    }

    @Transactional(readOnly = true)
    public AccountDetailsResponse getAccount(final Long accountId) {
        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        final Set<String> registeredSocialAccounts = account.getSocialAccounts().stream()
                .map(SocialAccount::getProvider).collect(Collectors.toSet());
        final Profile ownProfile = account.getOwnProfile();
        ownProfile.getId();
        ownProfile.getProfileImage();
        ownProfile.getNickname();
        final ProfileImage profileImage = ownProfile.getProfileImage();
        return AccountDetailsResponse.of(account);
    }

    // TODO move on query service later when QueryDSL gets ready.
    @Transactional(readOnly = true)
    public Optional<Account> getAccountBySocialProviderId(final String providerId) {
        Optional<Account> found = accountRepository.findBySocialAccountsProviderId(providerId);
        if (found.isPresent()) {
            System.out.println(found.get().getRoles()); // for query
        }
        return found;
    }

    @Transactional
    public AccountCreateResponse createDaoAccount(final AccountCreate request) {
        if (!isAvailableEmail(request.email())) {
            throw new ResourceAlreadyExistsException(Account.class, request.email());
        }
        if (profileRepository.existsByNickname(request.nickname())) {
            throw new ResourceAlreadyExistsException(Profile.class, request.nickname());
        }
        final Account newAccount = Account.builder()
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .birthDate(request.birthDate())
                .fullname(request.fullname())
                .roles(Set.of(Role.USER))
                .nickname(request.nickname())
                .attributes(request.attributes())
                .build();
        newAccount.daoSignedUp();
        accountRepository.save(newAccount);
        return AccountCreateResponse.builder()
                .sub(newAccount.getId())
                .profileId(newAccount.getOwnProfile().getId())
                .build();
    }

    @Transactional
    public Account createSocialAccount(final OAuth2UserInfo userInfo) {
        final Account account = accountRepository.findBySocialAccountsProviderId(userInfo.getProviderId())
                .orElse(Account.builder()
                        .email(UUID.randomUUID().toString())
                        .password(UUID.randomUUID().toString())
                        .nickname(UUID.randomUUID().toString())
                        .roles(Set.of(Role.USER))
                        .build());
        account.addSocialAccount(SocialAccount.builder()
                .daoAccount(account)
                .providerId(userInfo.getProviderId())
                .provider(userInfo.getProvider())
                .email(userInfo.getEmail())
                .build());
        return accountRepository.save(account);
    }

    @Transactional
    public Account connectSocialAccount(final Long accountId, final OAuthAccountConnect request) {
        final Account originAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("account with id " + accountId + " not found"));
        final String targetProvider = request.oauthProvider();
        originAccount.getSocialAccounts().stream()
                .filter(a -> a.getProvider().equals(targetProvider))
                .findAny()
                .ifPresent(ex -> {
                    throw new IllegalArgumentException("already registered social provider");
                });
        final Account targetDaoAccount = accountRepository.findById(request.targetAccountId())
                .orElseThrow(() -> new IllegalArgumentException("target account not exists"));
        final SocialAccount targetSocialAccount = targetDaoAccount.getSocialAccounts().stream()
                .filter(a -> a.getProvider().equals(targetProvider))
                .findAny().orElseThrow(() -> new IllegalArgumentException("Illegal social account connect request"));
        originAccount.addSocialAccount(SocialAccount.builder()
                .providerId(targetSocialAccount.getProviderId())
                .provider(targetProvider)
                .daoAccount(originAccount)
                .email(targetSocialAccount.getEmail())
                .build());
        accountRepository.delete(targetDaoAccount);
        return originAccount;
    }

    @Transactional
    public void updateAccountPassword(final Long accountId, final AccountPasswordUpdate request) {
        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        account.updatePassword(passwordEncoder.encode(request.password()));
    }

    @Transactional
    public void completeAccountRegister(final Long accountId, final CompleteAccountRegister request) {
        if (profileRepository.existsByNickname(request.nickname())) {
            throw new IllegalArgumentException("nickname already exists");
        }
        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("account not found"));
        account.getOwnProfile().updateNickname(request.nickname());
        account.updateFullname(request.fullname());
        account.updatePassword(passwordEncoder.encode(request.password()));
        account.daoSignedUp();
    }

    @Transactional
    public void deleteConnectedSocialAccount(final Long accountId, final Long socialAccountId) {
        final Account foundAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("account not found"));
        final SocialAccount socialAccount = foundAccount.getSocialAccounts().stream()
                .filter(a -> a.getId().equals(socialAccountId))
                .findAny().orElseThrow(() -> new IllegalArgumentException("social account not found"));
        foundAccount.getSocialAccounts().remove(socialAccount);
    }

    @Transactional
    public void deleteAccount(final Long accountId) {
        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        accountRepository.delete(account);
    }

}
