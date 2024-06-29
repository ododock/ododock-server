package ododock.webserver.security.config;

import lombok.RequiredArgsConstructor;
import ododock.webserver.security.handler.OAuth2LoginSuccessHandler;
import ododock.webserver.security.service.AuthService;
import ododock.webserver.security.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class OAuth2SecurityConfig {

    private final AuthService authService;
    private final JwtService jwtService;
    private final JwtDecoder jwtDecoder;

    @Bean
    public SecurityFilterChain oauth2Chain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                        .anyRequest()
                        .authenticated()
                )
                .oauth2Login(login -> login.userInfoEndpoint(
                        info -> info.userService(authService)
                ).successHandler(oAuth2LoginSuccessHandler(jwtService))
                )

                .oauth2ResourceServer(
                        resourceServer -> resourceServer.jwt(
                                jwt -> jwt.decoder(jwtDecoder)
                        )
                )
                .build();
    }

    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler(final JwtService jwtService) {
        return new OAuth2LoginSuccessHandler(jwtService);
    }

}
