package ododock.webserver.security.service;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.account.Account;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.request.OAuthAccountConnect;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class OAuthAccountConnectService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AccountRepository accountRepository;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final DefaultOAuth2UserService userService;

    public OAuthAccountConnectService(AccountRepository accountRepository, ClientRegistrationRepository clientRegistrationRepository) {
        this.accountRepository = accountRepository;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.userService = new DefaultOAuth2UserService();
    }

    public void connectAccount(final Long accountId, final OAuthAccountConnect request) {
        Account foundAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));

//        new OAuth2UserRequest();


    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        RestTemplate restTemplate = new RestTemplate();
        String userInfoEndpointUrl = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                userInfoEndpointUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> userAttributes = response.getBody();
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), userAttributes, "id");
    }
}
