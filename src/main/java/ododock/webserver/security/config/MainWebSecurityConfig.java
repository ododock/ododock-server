package ododock.webserver.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ododock.webserver.security.*;
import ododock.webserver.security.filter.DaoAuthenticationFilter;
import ododock.webserver.security.filter.HandlerExceptionResolverFilter;
import ododock.webserver.security.filter.RefreshTokenAuthenticationFilter;
import ododock.webserver.security.handler.DaoAuthenticationSuccessHandler;
import ododock.webserver.security.handler.OAuth2LoginSuccessHandler;
import ododock.webserver.web.ResourcePath;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProperties.class)
public class MainWebSecurityConfig {

    private final AuthService authService;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final JwtDecoder jwtDecoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final SecurityProperties securityProperties;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web
                .ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations(),
                        new AntPathRequestMatcher("/docs/**"));
    }

    @Bean
    MvcRequestMatcher.Builder mvc(final HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);

        http
                .addFilterBefore(new HandlerExceptionResolverFilter(handlerExceptionResolver), SecurityContextHolderFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .logout(c -> c
                        .logoutUrl(ResourcePath.AUTH_LOGOUT_URL)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("http://localhost:3000"))
                .sessionManagement(c -> c
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(c -> c
                        .requestMatchers(RequestPathMatcher.PERMIT_ALL_MATCHER).permitAll()
                        .requestMatchers(RequestPathMatcher.AUTHENTICATED_MATCHER).authenticated()
                        .requestMatchers(ResourcePath.ACTUATOR).denyAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(new RethrowAuthenticationEntryPoint())
                        .accessDeniedHandler(new RethrowAuthorizationEntryPoint())
                )
                .addFilterBefore(
                        new DaoAuthenticationFilter(
                                authenticationManagerBuilder.getOrBuild(),
                                daoAuthenticationSuccessHandler(jwtService, objectMapper)
                        ),
                        OAuth2AuthorizationRequestRedirectFilter.class
                )
                .addFilterBefore(
                        new RefreshTokenAuthenticationFilter(jwtDecoder, jwtService, objectMapper),
                        BearerTokenAuthenticationFilter.class
                )
                .oauth2Login(login ->
                        login.userInfoEndpoint(
                                info -> info.userService(authService)
                        ).successHandler(oAuth2LoginSuccessHandler(jwtService))
                )
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(
                        jwt -> jwt.decoder(jwtDecoder)
                ).authenticationEntryPoint(new RethrowAuthenticationEntryPoint())
                                .accessDeniedHandler(new RethrowAuthorizationEntryPoint())
                );

        return http.build();
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
        configuration.setAllowedOrigins(this.securityProperties.allowedOrigins());
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
