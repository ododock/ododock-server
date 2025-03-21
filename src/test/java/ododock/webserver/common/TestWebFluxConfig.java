package ododock.webserver.common;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.web.exceptionhandler.WebFluxExceptionHandler;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResolverFactory;
import ododock.webserver.web.exceptionhandler.builder.ExceptionResponseResolverBuilder;
import ododock.webserver.web.exceptionhandler.builder.LogLevelResolverBuilder;
import ododock.webserver.web.exceptionhandler.factories.*;
import ododock.webserver.web.exceptionhandler.log.CompositeLogLevelResolver;
import ododock.webserver.web.exceptionhandler.response.CompositeExceptionResponseResolver;
import ododock.webserver.web.v1alpha1.StatusConverter;
import ododock.webserver.web.v1alpha1.V1alpha1ArticleController;
import ododock.webserver.web.v1alpha1.V1alpha1CategoryController;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
//@TestConfiguration
public class TestWebFluxConfig {

    @ControllerAdvice(basePackageClasses = {V1alpha1ArticleController.class, V1alpha1CategoryController.class})
    @AllArgsConstructor
    public static class WebFluxGlobalExceptionHandler {
        @NonNull
        private final WebFluxExceptionHandler webFluxExceptionHandler;

        @ExceptionHandler
        public Mono<ResponseEntity<Object>> handle(ServerWebExchange exchange, Exception e) {
            return webFluxExceptionHandler.handle(exchange, e);
        }
    }

    private static List<ExceptionResolverFactory> exceptionResolverFactories() {
        return List.of(
                new ResourceNotFoundExceptionResolverFactory(),
                new ResourceConflictExceptionResolverFactory(),
                new AccessDeniedExceptionResolverFactory(),
                new AuthenticationExceptionResolverFactory(),
                new BadCredentialsExceptionResolverFactory(),
                new IllegalPropertyExceptionResolverFactory(),
                new InsufficientAuthenticationExceptionResolverFactory(),
                new MethodArgumentTypeMismatchExceptionResolverFactory(),
                new OAuth2AuthenticationExceptionResolverFactory(),
                new SpringMvcExceptionResolverFactory(),
                new SpringWebFluxExceptionResolverFactory(),
                new VerificationCodeExceptionResolverFactory()
        );
    }

    private static CompositeExceptionResponseResolver compositeMvcExceptionResponseResolver() {
        return new ExceptionResponseResolverBuilder()
                .addAllFactories(exceptionResolverFactories())
                .build();
    }

    private static CompositeExceptionResponseResolver compositeWebFluxExceptionResponseResolver() {
        return new ExceptionResponseResolverBuilder()
                .addAllFactories(exceptionResolverFactories())
                .build();
    }

    private static CompositeLogLevelResolver compositeLogLevelResolver() {
        return new LogLevelResolverBuilder()
                .addAllFactories(exceptionResolverFactories())
                .build();
    }

    @Bean
    public WebFluxExceptionHandler webFluxExceptionHandler() {
        return new WebFluxExceptionHandler(
                compositeWebFluxExceptionResponseResolver(),
                new StatusConverter()::convert,
                compositeLogLevelResolver());
    }

}
