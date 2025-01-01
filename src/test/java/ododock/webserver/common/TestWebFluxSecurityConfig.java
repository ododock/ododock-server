package ododock.webserver.common;

import ododock.webserver.web.ResourcePath;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@TestConfiguration
@EnableWebFluxSecurity
public class TestWebFluxSecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // PERMIT_ALL_MATCHER에 해당하는 패턴들
                        .pathMatchers(HttpMethod.GET, ResourcePath.DOCS + "/**").permitAll()

                        // OAuth 관련
                        .pathMatchers(ResourcePath.OAUTH2 + "/authorization/**").permitAll()
                        .pathMatchers(ResourcePath.OAUTH_CALLBACK).permitAll()
                        .pathMatchers(HttpMethod.POST, ResourcePath.AUTH_PROCESSING_URL).permitAll()
                        .pathMatchers(HttpMethod.POST, ResourcePath.AUTH_LOGOUT_URL).permitAll()

                        // API 관련
                        .pathMatchers(HttpMethod.POST, ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.VERIFICATION + "/*").permitAll()

                        // Account 관련
                        .pathMatchers(HttpMethod.GET, ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS).permitAll()
                        .pathMatchers(HttpMethod.POST, ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS).permitAll()
                        .pathMatchers(HttpMethod.POST, ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS + ResourcePath.VERIFICATION).permitAll()
                        .pathMatchers(HttpMethod.GET, ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS + "/**").permitAll()
                        .pathMatchers(HttpMethod.PUT, ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS + "/*" + ResourcePath.ACCOUNTS_SUBRESOURCE_PASSWORD).permitAll()

                        // Curations 관련
                        .pathMatchers(HttpMethod.GET, ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.CURATIONS + "/**").permitAll()

                        // AUTHENTICATED_MATCHER에 해당하는 패턴
                        .pathMatchers(ResourcePath.API + "/**").authenticated()

                        // 그 외 모든 요청
                        .anyExchange().authenticated()
                )
                .build();
    }

}
