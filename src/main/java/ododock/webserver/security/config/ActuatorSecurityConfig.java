package ododock.webserver.security.config;

import lombok.RequiredArgsConstructor;
import ododock.webserver.security.RethrowAuthenticationEntryPoint;
import ododock.webserver.security.filter.HandlerExceptionResolverFilter;
import ododock.webserver.web.ResourcePath;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@EnableConfigurationProperties(BasicAuthProperties.class)
@RequiredArgsConstructor
public class ActuatorSecurityConfig {

    private final BasicAuthProperties basicAuthProperties;
    private final PasswordEncoder passwordEncoder;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .addFilterBefore(new HandlerExceptionResolverFilter(handlerExceptionResolver), SecurityContextHolderFilter.class)
                .securityMatcher(ResourcePath.ACTUATOR)
                .authorizeHttpRequests(
                        auth -> auth.anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(c -> c.authenticationEntryPoint(new RethrowAuthenticationEntryPoint()))
                .formLogin(AbstractHttpConfigurer::disable)
                .userDetailsService(userDetailsService(basicAuthProperties))
                .build();
    }

    public UserDetailsService userDetailsService(BasicAuthProperties basicAuthProperties) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        if (!basicAuthProperties.getUsers().isEmpty()) {
            for (BasicAuthProperties.BasicUser user : basicAuthProperties.getUsers()) {
                UserDetails userDetails = User
                        .withUsername(user.getUsername())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .build();
                manager.createUser(userDetails);
            }
        }
        return manager;
    }

}
