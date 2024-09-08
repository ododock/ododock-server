package ododock.webserver.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ododock.webserver.security.filter.DaoAuthenticationFilter;
import ododock.webserver.security.filter.RefreshTokenAuthenticationFilter;
import ododock.webserver.security.handler.DaoAuthenticationSuccessHandler;
import ododock.webserver.security.handler.OAuth2LoginSuccessHandler;
import ododock.webserver.security.service.AuthService;
import ododock.webserver.security.service.JwtService;
import ododock.webserver.security.util.RequestParameterMatcher;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class MainWebSecurityConfig {

    private final AuthService authService;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final JwtDecoder jwtDecoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

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
    public WebMvcConfigurer webConfig() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .logout(c -> c
                        .logoutUrl("/api/v1/auth/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("http://localhost:3000"))
                .sessionManagement(c -> c
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(c -> c
                        .requestMatchers(
                                new RequestParameterMatcher(HttpMethod.GET, "/api/v1/accounts", List.of("email"))
                        ).permitAll()
                        .requestMatchers(
                                new RequestParameterMatcher(HttpMethod.GET, "/api/v1/profiles", List.of("nickname"))
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/accounts/{userId}/verification-code").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/accounts/{userId}/verification-code").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/accounts").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.GET, "/oauth2/**").permitAll()
                        .requestMatchers("/static/docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler(new AccessDeniedHandlerImpl())
                )
                .addFilterAt(
                        new DaoAuthenticationFilter(
                                authenticationManagerBuilder.getOrBuild(),
                                daoAuthenticationSuccessHandler(jwtService, objectMapper)
                        ),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        new RefreshTokenAuthenticationFilter(jwtDecoder, jwtService, objectMapper),
                        UsernamePasswordAuthenticationFilter.class
                )
                .oauth2Login(login ->
                        login.userInfoEndpoint(
                                info -> info.userService(authService)
                        ).successHandler(oAuth2LoginSuccessHandler(jwtService))
                )
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(
                        jwt -> jwt.decoder(jwtDecoder)
                ));

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

}
