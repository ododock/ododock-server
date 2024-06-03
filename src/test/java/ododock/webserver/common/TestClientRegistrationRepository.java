package ododock.webserver.common;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestClientRegistrationRepository implements ClientRegistrationRepository {

    private List<ClientRegistration> clientRegistrations;

    public TestClientRegistrationRepository() {
        this.clientRegistrations = new ArrayList<>();
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("testRegistrationId")
                .clientId("testClientId")
                .clientSecret("testClientSecret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/login/oauth2/code/testRegistrationId")
                .scope("read", "write")
                .authorizationUri("https://example.com/oauth2/authorize")
                .tokenUri("https://example.com/oauth2/token")
                .userInfoUri("https://example.com/oauth2/userinfo")
                .userNameAttributeName("sub")
                .clientName("testClientName")
                .build();
        clientRegistrations.add(clientRegistration);
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        return clientRegistrations.stream()
                .filter(registration -> registration.getRegistrationId().equals(registrationId))
                .findFirst()
                .orElse(null);
    }

}
