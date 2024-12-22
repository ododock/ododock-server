package ododock.webserver.config.domain;

import ododock.webserver.config.ContentClientProperties;
import ododock.webserver.config.CurationClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;

@EnableAsync
@EnableConfigurationProperties({ContentClientProperties.class, CurationClientProperties.class})
@Configuration
public class DomainServiceConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestClient contentClient() {
        return RestClient.builder().build();
    }

    // web

    // domain
    //    curation
    //    content

}
