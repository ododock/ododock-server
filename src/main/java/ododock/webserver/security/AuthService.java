package ododock.webserver.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.SocialAccountService;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.security.response.DaoUserDetails;
import ododock.webserver.security.response.OAuth2UserInfo;
import ododock.webserver.security.util.OAuth2UserMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AccountRepository accountRepository;
    private final SocialAccountService socialAccountService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "is not found"));
        return new DaoUserDetails(account);
    }

    @Transactional
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        final OAuth2User oAuth2User = delegate.loadUser(userRequest);

        final String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        final OAuth2UserInfo userInfo = OAuth2UserMapper.resolveUserInfo(userRequest, oAuth2User);
        final Optional<Account> foundDaoAccount = accountRepository.findAccountWithRolesBySocialAccountsProviderId(userInfo.getProviderId());

        if (foundDaoAccount.isPresent()) {
            final Long accountId = foundDaoAccount.get().getId();
            userInfo.addAttribute("accountId", accountId);
            OAuth2User user = new DefaultOAuth2User(
                    foundDaoAccount.get().getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                            .collect(Collectors.toList()),
                    userInfo.getAttributes(),
                    userNameAttributeName);
            return user;
        }

        final Account newAccount = socialAccountService.createSocialAccount(userInfo);
        userInfo.addAttribute("accountId", newAccount.getId());
        return new DefaultOAuth2User(
                newAccount.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                        .collect(Collectors.toList()),
                userInfo.getAttributes(),
                userNameAttributeName);
    }

}
