package ododock.webserver.common;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@TestConfiguration
public class RestDocsConfig {

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return configurer -> configurer
                .operationPreprocessors()
                .withRequestDefaults(
                        prettyPrint(),
                        modifyUris()
                                .scheme("http")
                                .host("dev.ododock.com")
                                .removePort()
                )
                .withResponseDefaults(
                        prettyPrint()
                );
    }

}
