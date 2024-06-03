package ododock.webserver.common;

import ododock.webserver.security.request.RequestParameterMatcher;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http, final MvcRequestMatcher.Builder mvc) throws Exception {
        final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);
        http
                .requestCache(config -> config.requestCache(requestCache))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/accounts/{accountId}")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/accounts")).permitAll()
                        .requestMatchers(new RequestParameterMatcher(
                                HttpMethod.GET, "/api/v1/accounts", List.of("email"))).permitAll()
                        .requestMatchers(new RequestParameterMatcher(
                                HttpMethod.GET, "/api/v1/profiles", List.of("nickname"))).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/auth/login")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/auth/logout")).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

}
