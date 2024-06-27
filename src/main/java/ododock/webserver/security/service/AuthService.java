package ododock.webserver.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.Account;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.security.response.DaoUserDetails;
import ododock.webserver.security.response.OAuth2UserInfo;
import ododock.webserver.security.util.OAuth2UserMapper;
import ododock.webserver.service.AccountService;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "is not found"));
        final DaoUserDetails daoUserDetails = new DaoUserDetails(account);
        return daoUserDetails;
    }

    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        final OAuth2User oAuth2User = delegate.loadUser(userRequest);

        final String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        final OAuth2UserInfo userInfo = OAuth2UserMapper.resolveUserInfo(userRequest, oAuth2User);
        final Optional<Account> foundDaoAccount = accountRepository.findBySocialAccountsProviderId(userInfo.getProviderId());

        if (foundDaoAccount.isPresent()) {
            final Long accountId = foundDaoAccount.get().getId();
            userInfo.addAttribute("accountId", accountId);
            return new DefaultOAuth2User(
                    foundDaoAccount.get().getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                            .collect(Collectors.toList()),
                    userInfo.getAttributes(),
                    userNameAttributeName
            );
        }

        final Account newAccount = accountService.createSocialAccount(userInfo);
        userInfo.addAttribute("accountId", newAccount.getId());
        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("OAUTH2_USER")),
                userInfo.getAttributes(),
                userNameAttributeName);
    }

}
