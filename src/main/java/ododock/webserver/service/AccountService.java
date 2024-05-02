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
import ododock.webserver.response.AccountCreateResponse;
import ododock.webserver.response.AccountDetailsResponse;
import ododock.webserver.security.response.OAuth2UserInfo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final ProfileService profileService;

    @Transactional(readOnly = true)
    public boolean isAvailableEmail(final String email) {
        return !accountRepository.existsByEmail(email) && !accountRepository.existsBySocialAccountsEmail(email);
    }

    @Transactional(readOnly = true)
    public AccountDetailsResponse getAccount(final Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        return AccountDetailsResponse.of(account);
    }

    // TODO move on query service later when QueryDSL gets ready.
    @Transactional(readOnly = true)
    public Optional<Account> getAccountWithSocialAccountEmail(final String email) {
        Optional<Account> found = accountRepository.findBySocialAccountsEmail(email);
        if (found.isPresent()) {
            System.out.println(found.get().getRoles());
        }
        return found;
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
                .attributes(request.attributes())
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
    public Account createSocialAccount(final OAuth2UserInfo userInfo) {
        System.out.println(userInfo);
        Account newAccount = Account.builder()
                .password(UUID.randomUUID().toString())
                .email(userInfo.getEmail())
                .birthDate(resolveDate(userInfo.getBirthYear(), userInfo.getBirthday()))
                .fullname(userInfo.getName())
                .roles(Set.of(Role.USER))
                .nickname(userInfo.getNickname() == null
                        ? UUID.randomUUID().toString().split("-")[0]
                        : userInfo.getNickname()
                )
                .attributes(
                        userInfo.getGender() == null
                                ? null
                                : Map.of("gender", List.of(userInfo.getGender()))
                )
                .profileImage(resolveProfileImage(userInfo.getProfileImage()))
                .build();
        newAccount.addSocialAccount(SocialAccount.builder()
                .ownerAccount(newAccount)
                .email(userInfo.getEmail())
                .provider(userInfo.getProvider())
                .providerId(userInfo.getProviderId())
                .build());
        return accountRepository.save(newAccount);
//        return AccountCreateResponse.builder()
//                .accountId(newAccount.getId())
//                .profileId(newAccount.getOwnProfile().getId())
//                .build();
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

    private LocalDate resolveDate(final String birthYear, final String birthday) {
        if (birthYear == null || birthday == null) {
            return null;
        }
        return LocalDate.of(
                Integer.valueOf(birthYear),
                Integer.valueOf(birthday.split("-")[0]),
                Integer.valueOf(birthday.split("-")[1])
        );
    }

    private ProfileImage resolveProfileImage(final String profileImageUrl) {
        return profileImageUrl == null
                ? null
                : ProfileImage.builder()
                .imageSource(profileImageUrl)
                .fileType(profileImageUrl.substring(
                        profileImageUrl.lastIndexOf('.') + 1)
                )
                .build();
    }

}
