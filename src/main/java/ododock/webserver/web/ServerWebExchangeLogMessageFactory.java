package ododock.webserver.web;

import lombok.NonNull;
import ododock.webserver.web.exceptionhandler.ExceptionLogMessageFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;

import java.security.Principal;
import java.util.Optional;

public class ServerWebExchangeLogMessageFactory {

    private static final String EXCEPTION_OCCURRED_MESSAGE_TEMPLATE = "Exception occurred Processing request [%s] with authentication [%s]";

    private static String createExceptionOccurredMessage(ServerWebExchange exchange) {
        return String.format(EXCEPTION_OCCURRED_MESSAGE_TEMPLATE, createRequestDescription(exchange), createAuthenticationDescription(exchange));
    }

    private static String createRequestDescription(final ServerWebExchange exchange) {
        return String.format(
                "method=%s uri=%s client=%s",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                exchange.getRequest().getRemoteAddress());
    }

    private static String createAuthenticationDescription(ServerWebExchange exc) {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Principal::getName)
                .orElse("NotAuthenticated");
    }

    @NonNull
    private final ExceptionLogMessageFactory exceptionLogMessageFactory;

    public ServerWebExchangeLogMessageFactory() {
        this.exceptionLogMessageFactory = new ExceptionLogMessageFactory();
    }

    public String createMessage(ServerWebExchange exchange, Exception e, boolean includeStackTrace) {
        // "Exception occurred Processing request [%s] with authentication [%s]";
        return createExceptionOccurredMessage(exchange) +
                "\n" +
                this.exceptionLogMessageFactory.createExceptionLogMessage(e, includeStackTrace);
    }

}
