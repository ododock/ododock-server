package ododock.webserver.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ododock.webserver.security.filter.DaoAuthenticationFilter;
import ododock.webserver.security.handler.DaoAuthenticationSuccessHandler;
import ododock.webserver.security.handler.OAuth2LoginSuccessHandler;
import ododock.webserver.security.request.RequestParameterMatcher;
import ododock.webserver.security.service.AuthService;
import ododock.webserver.security.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final AuthService authService;
    private final JwtService jwtService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtDecoder jwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http, final MvcRequestMatcher.Builder mvc) throws Exception {
        final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);
        http
                .requestCache(config -> config.requestCache(requestCache))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(config -> config.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/docs/**").permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/accounts/{accountId}")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/accounts")).permitAll()
                        .requestMatchers(new RequestParameterMatcher(
                                HttpMethod.GET, "/api/v1/accounts", List.of("email"))).permitAll()
                        .requestMatchers(new RequestParameterMatcher(
                                HttpMethod.GET, "/api/v1/profiles", List.of("nickname"))).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/auth/login")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/auth/logout")).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        // TODO EntryPoint 설정: ododock 도메인의 로그인 페이지 url
                        // TODO FailureHandler 설정: ododock 로그인 url
                        .userInfoEndpoint(oauth ->
                                oauth.userService(authService)
                        )
                        .successHandler(oAuth2LoginSuccessHandler(jwtService))
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder)
                        )
                )
                .addFilterBefore(
                        new DaoAuthenticationFilter(
                                authenticationManagerBuilder.getOrBuild(),
                                daoAuthenticationSuccessHandler(jwtService, objectMapper)
                        ),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(final HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public DaoAuthenticationSuccessHandler daoAuthenticationSuccessHandler(
            final JwtService jwtService,
            final ObjectMapper objectMapper
    ) {
        return new DaoAuthenticationSuccessHandler(jwtService, objectMapper);
    }

    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler(final JwtService jwtService) {
        return new OAuth2LoginSuccessHandler(jwtService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000"); // 허용할 출처
        configuration.addAllowedOrigin("http://localhost:3000/home");
        configuration.addAllowedMethod("*"); // 허용할 HTTP 메소드
        configuration.addAllowedHeader("*"); // 허용할 HTTP 헤더
        configuration.setAllowCredentials(true); // 쿠키 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
