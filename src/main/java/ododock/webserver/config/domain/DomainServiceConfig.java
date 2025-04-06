package ododock.webserver.config.domain;

import ododock.webserver.domain.StorageService;
import ododock.webserver.domain.curation.LibraryBookListOptionsUtil;
import ododock.webserver.domain.profile.ProfileService;
import ododock.webserver.domain.profile.SimpleProfileService;
import ododock.webserver.repository.jpa.AccountRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@EnableAsync
@EnableConfigurationProperties({ContentClientProperties.class, LibraryCurationClientProperties.class})
@Configuration
public class DomainServiceConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ProfileService profileService(StorageService storageService, AccountRepository accountRepository) {
        return new SimpleProfileService(storageService, accountRepository);
    }

    @Bean
    public WebClient bookCurationClient(LibraryCurationClientProperties curationClientProperties) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(curationClientProperties.baseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);
        return WebClient.builder()
                .uriBuilderFactory(factory)
                .build();
    }

    @Bean
    public LibraryBookListOptionsUtil libraryBookListOptionsUtil(LibraryCurationClientProperties curationClientProperties) {
        return new LibraryBookListOptionsUtil(curationClientProperties.apiKey());
    }

}
