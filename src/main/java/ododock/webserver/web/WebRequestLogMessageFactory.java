package ododock.webserver.web;

import lombok.NonNull;
import ododock.webserver.web.exceptionhandler.ExceptionLogMessageFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;
import java.util.Optional;

public class WebRequestLogMessageFactory {

    private static final String EXCEPTION_OCCURRED_MESSAGE_TEMPLATE = "Exception occurred Processing request [%s] with authentication [%s]";

    private static String createExceptionOccurredMessage(WebRequest request) {
        return String.format(EXCEPTION_OCCURRED_MESSAGE_TEMPLATE, createRequestDescription(request), createAuthenticationDescription(request));
    }

    private static String createRequestDescription(final WebRequest request) {
        if (request instanceof ServletWebRequest) {
            ServletWebRequest casted = (ServletWebRequest) request;
            return String.format(
                    "method=%s uri=%s client=%s",
                    casted.getHttpMethod(),
                    casted.getRequest().getRequestURI(),
                    casted.getRequest().getRemoteAddr());
        }

        return request.getDescription(true);
    }

    private static String createAuthenticationDescription(WebRequest request) {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Principal::getName)
                .orElse("NotAuthenticated");
    }

    @NonNull
    private final ExceptionLogMessageFactory exceptionLogMessageFactory;

    public WebRequestLogMessageFactory() {
        this.exceptionLogMessageFactory = new ExceptionLogMessageFactory();
    }

    public String createMessage(WebRequest request, Exception e, boolean includeStackTrace) {
        // "Exception occurred Processing request [%s] with authentication [%s]";
        return createExceptionOccurredMessage(request) +
                "\n" +
                this.exceptionLogMessageFactory.createExceptionLogMessage(e, includeStackTrace);
    }

}
