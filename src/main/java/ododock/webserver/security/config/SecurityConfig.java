package ododock.webserver.security.config;

import jakarta.servlet.http.HttpServletRequest;
import ododock.webserver.security.JwtAuthenticationFilter;
import ododock.webserver.security.JwtTokenValidationFilter;
import ododock.webserver.security.JwtUtil;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.http.HttpMethod.GET;

@Configuration
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    public SecurityConfig(
            final AuthenticationConfiguration authenticationConfiguration,
            final JwtUtil jwtUtil
    ) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors((auth) -> auth
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();

                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);
                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                                return configuration;
                            }
                        }));

        http
                .csrf((auth)-> auth.disable());

        http
                .formLogin((auth) -> auth.disable());

        http
                .httpBasic((auth) -> auth.disable());

        http
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests(
                        (authorize) -> authorize
                                // request matcher는 상단부터 적용되기에, 제한의 범위가 좁은것에서 넓은방향으로 정의되어야함
                                .requestMatchers(
                                        "/api/v1/accounts/username",
                                        "/api/v1/accounts/email",
                                        "/api/v1/accounts/**",
                                        "/api/v1/auth/**"
                                ).permitAll()

                                .requestMatchers(
                                        GET, "/api/v1/accounts"
                                ).authenticated()

                                .requestMatchers(
                                        "/api/v1/articles/*"
                                ).authenticated()
                );

        http
                .addFilterAt(
                        new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                        UsernamePasswordAuthenticationFilter.class
                ).addFilterBefore(new JwtTokenValidationFilter(jwtUtil), JwtAuthenticationFilter.class);

        http
                .logout((auth) -> auth
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessUrl("/"));


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
