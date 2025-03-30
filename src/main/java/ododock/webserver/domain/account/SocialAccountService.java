package ododock.webserver.domain.account;

import lombok.RequiredArgsConstructor;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.security.response.OAuth2UserInfo;
import ododock.webserver.web.ResourceNotFoundException;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Account;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1SocialAccount;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
     * merge Social Account specified by request into given Account
     *
     * @param accountId
     * @param request   which contains OAuth2 Provider and Account ID will be merged
     * @return
     */
    @Transactional
    public Account mergeSocialAccount(final Long accountId, final V1alpha1SocialAccount request) {
        Assert.notNull(request.getAccountId(), "accountId must not be null");
        Assert.notNull(request.getProvider(), "provider must not be null");
        final Account originAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        final String targetProvider = request.getProvider();
        originAccount.getSocialAccounts().stream()
                .filter(a -> a.getProvider().equals(targetProvider))
                .findAny()
                .ifPresent(ex -> {
                    throw new IllegalArgumentException("already registered social provider");
                });
        final Account targetDaoAccount = accountRepository.findById(request.getAccountId())
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
    public void completeSocialAccountRegister(Long accountId, V1alpha1Account request) {
        Assert.notNull(request.getNickname(), "nickname must not be null");
        Assert.notNull(request.getFullname(), "fullname must not be null");
        Assert.notNull(request.getPassword(), "password must not be null");
        if (accountRepository.existsByOwnProfile_Nickname(request.getNickname())) {
            throw new IllegalArgumentException("nickname already exists");
        }
        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("account not found"));
        account.getOwnProfile().updateNickname(request.getNickname());
        account.getOwnProfile().updateFullname(request.getFullname());
        account.updatePassword(passwordEncoder.encode(request.getPassword()));
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
