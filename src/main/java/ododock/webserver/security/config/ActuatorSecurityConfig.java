package ododock.webserver.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@EnableConfigurationProperties(BasicAuthProperties.class)
@RequiredArgsConstructor
public class ActuatorSecurityConfig {

    private final BasicAuthProperties basicAuthProperties;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(userDetailsService(basicAuthProperties))
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/actuator/**").authenticated()
                ).build();
    }

    public UserDetailsService userDetailsService(BasicAuthProperties basicAuthProperties) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        for (BasicAuthProperties.BasicUser user : basicAuthProperties.getUsers()) {
            UserDetails userDetails = User
                    .withUsername(user.getUsername())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .build();
            manager.createUser(userDetails);
        }
        return manager;
    }

}
