package ododock.webserver.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.Account;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.security.DaoOAuth2User;
import ododock.webserver.security.DaoUserDetails;
import ododock.webserver.security.response.GoogleUserInfo;
import ododock.webserver.security.response.NaverUserInfo;
import ododock.webserver.security.response.OAuth2UserInfo;
import ododock.webserver.service.AccountService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
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
        log.info("Auth Service find user by email " + email);
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "is not found"));
        DaoUserDetails daoUserDetails = new DaoUserDetails(account);
        return daoUserDetails;
    }

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        /**
         * 이 시점은 이미 authentication이 완료된 시점임.
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registartionId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2UserInfo userInfo = userInfoResolver(userRequest, oAuth2User);
        Optional<Account> foundAccount = accountService.getAccountWithSocialAccountEmail(userInfo.getEmail());
        Map<String, Object> attributes = new HashMap<>((oAuth2User.getAttributes()));

        if (foundAccount.isPresent()) {
            // 소셜 로그인된 계정이 존재하는 경우, 정상 소셜로그인 처리
            Long accountId = foundAccount.get().getId();
            userInfo.addAttribute("accountId", accountId);
            addUserAttribute(attributes, "accountId", accountId);
            // TODO 소셜 계정의 데이터를 기반으로 업데이트
            return new DaoOAuth2User(
                    foundAccount.get().getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                            .collect(Collectors.toList()),
                    attributes,
                    userNameAttributeName,
                    registartionId
            );
        }

        Optional<Account> existingAccount = accountRepository.findByEmail(userInfo.getEmail());

        if (existingAccount.isEmpty()) {
            if (existingAccount.isEmpty()) {
                // 신규 유저인 경우, 회원가입
                Account newAccount = accountService.createSocialAccount(userInfo);
                userInfo.addAttribute("accountId", newAccount.getId());
                addUserAttribute(attributes, "accountId", newAccount.getId());
                return new DaoOAuth2User(
                        newAccount.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                                .collect(Collectors.toList()),
                        attributes,
                        userNameAttributeName,
                        registartionId
                );
            } else {
                //TODO 소셜 이메일로 일반 회원가입은 했지만, 소셜 연동이 안된 케이스
                //TODO FailuerHandler에서 연동안내 페이지로 redirect
                throw new OAuth2AuthenticationException("{\"error\": \"social_account_not_linked\"}");

            }
        }
        return null;
    }

    private OAuth2UserInfo userInfoResolver(final OAuth2UserRequest request, final OAuth2User user) {
        String registartionId = request.getClientRegistration().getRegistrationId();
        if (registartionId.equals("naver")) {
            return new NaverUserInfo((Map<String, Object>) user.getAttributes().get("response"));
        } else if (registartionId.equals("google")) {
            return new GoogleUserInfo(user.getAttributes());
        } else {
            return null;
        }
    }

    private Map<String, Object> addUserAttribute(final Map<String, Object> oauthAttributes,
                                                 final String key,
                                                 final Object value
    ) {
        Map<String, Object> mergedAttributes = (Map<String, Object>) new HashMap<>(oauthAttributes).get("response");
        mergedAttributes.put(key, String.valueOf(value));
        return mergedAttributes;
    }

}
