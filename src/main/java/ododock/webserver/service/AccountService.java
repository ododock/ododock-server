package ododock.webserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.Role;
import ododock.webserver.domain.account.SocialAccount;
import ododock.webserver.domain.account.VerificationInfo;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.exception.InvalidAccountPropertyException;
import ododock.webserver.exception.InvalidVerificationCodeException;
import ododock.webserver.exception.ResourceAlreadyExistsException;
import ododock.webserver.exception.ResourceNotFoundException;
import ododock.webserver.exception.VerificationCodeExpiredException;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.repository.ProfileRepository;
import ododock.webserver.request.account.AccountCreate;
import ododock.webserver.request.account.AccountPasswordReset;
import ododock.webserver.request.account.AccountPasswordUpdate;
import ododock.webserver.request.account.CompleteDaoAccountVerification;
import ododock.webserver.request.account.CompleteSocialAccountRegister;
import ododock.webserver.request.account.OAuthAccountConnect;
import ododock.webserver.response.account.AccountCreateResponse;
import ododock.webserver.response.account.AccountVerified;
import ododock.webserver.security.response.OAuth2UserInfo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final MailService mailService;

    @Transactional(readOnly = true)
    public boolean isAvailableEmail(final String email) {
        return !accountRepository.existsByEmail(email) && !accountRepository.existsBySocialAccountsEmail(email);
    }

    @Transactional
    public AccountCreateResponse createDaoAccount(final AccountCreate request) throws Exception {
        if (!isAvailableEmail(request.email())) {
            throw new ResourceAlreadyExistsException(Account.class, request.email());
        }
        if (profileRepository.existsByNickname(request.nickname())) {
            throw new ResourceAlreadyExistsException(Profile.class, request.nickname());
        }
        final Account newAccount = Account.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .birthDate(request.birthDate())
                .fullname(request.fullname())
                .roles(Set.of(Role.USER))
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
        final Account account = accountRepository.findAccountWithRolesBySocialAccountsProviderId(userInfo.getProviderId())
                .orElse(Account.builder()
                        .email(userInfo.getEmail())
                        .password(null)
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

    /**
     * merge Social Account specified by {@link ododock.webserver.request.account.OAuthAccountConnect} into given Account
     *
     * @param accountId
     * @param request   which contains OAuth2 Provider and Account ID will be merged
     * @return
     */
    @Transactional
    public Account connectSocialAccount(final Long accountId, final OAuthAccountConnect request) {
        final Account originAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("DB Account with id not found", accountId));
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
    public void resetAccountPassword(final Long accountId, final AccountPasswordReset request) throws InvalidVerificationCodeException, VerificationCodeExpiredException {
        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        if (account.getVerificationInfo() == null) {
            throw new InvalidVerificationCodeException("verification code not found");
        }
        account.getVerificationInfo().validate(request.code());
        account.updatePassword(passwordEncoder.encode(request.newPassword()));
    }

    @Transactional
    public void sendEmailVerificationCode(final Long accountId, final String email) throws Exception {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        if (!account.getEmail().equalsIgnoreCase(email)) {
            throw new InvalidAccountPropertyException(email);
        }
        VerificationInfo verificationInfo = account.generateVerificationCode();
        mailService.sendVerificationCode(account.getEmail(), verificationInfo.getCode());
    }

    @Transactional
    public void completeSocialAccountRegister(Long accountId, CompleteSocialAccountRegister request) {
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
    public AccountVerified verifyDaoAccountEmail(final Long userId, final CompleteDaoAccountVerification request) throws InvalidVerificationCodeException, VerificationCodeExpiredException {
        Account foundAccount = accountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, userId));
        if (foundAccount.getVerificationInfo() == null) {
            throw new InvalidVerificationCodeException("verification code not found");
        }
        if (foundAccount.getVerificationInfo().validate(request.code())) {
            log.info("user has been activated");
            foundAccount.activate();
            return AccountVerified.of(foundAccount.generateResetPasswordCode());
        }
        throw new InvalidVerificationCodeException("Illegal account verification request");
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
