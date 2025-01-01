package ododock.webserver.domain.account;

import lombok.RequiredArgsConstructor;
import ododock.webserver.web.exception.ResourceNotFoundException;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.security.response.OAuth2UserInfo;
import ododock.webserver.web.v1alpha1.dto.account.CompleteSocialAccountRegister;
import ododock.webserver.web.v1alpha1.dto.account.OAuthAccountMerge;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SocialAccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

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
        account.verifyEmail();
        return accountRepository.save(account);
    }

    /**
     * merge Social Account specified by {@link OAuthAccountMerge} request into given Account
     *
     * @param accountId
     * @param request   which contains OAuth2 Provider and Account ID will be merged
     * @return
     */
    @Transactional
    public Account mergeSocialAccount(final Long accountId, final OAuthAccountMerge request) {
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
    public void completeSocialAccountRegister(Long accountId, CompleteSocialAccountRegister request) {
        if (accountRepository.existsByOwnProfile_Nickname(request.nickname())) {
            throw new IllegalArgumentException("nickname already exists");
        }
        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("account not found"));
        account.getOwnProfile().updateNickname(request.nickname());
        account.getOwnProfile().updateFullname(request.fullname());
        account.updatePassword(passwordEncoder.encode(request.password()));
        account.activate();
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

}
