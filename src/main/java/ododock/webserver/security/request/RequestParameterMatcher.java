package ododock.webserver.security.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestParameterMatcher implements RequestMatcher {

    private HttpMethod method;
    private String pattern;
    private Set<String> parameters;

    public RequestParameterMatcher(final HttpMethod method, final String pattern, final Collection<String> parameters) {
        this.method = method;
        this.pattern = pattern;
        this.parameters = parameters.stream().collect(Collectors.toSet());
    }

    @Override
    public boolean matches(final HttpServletRequest request) {
        if (!request.getRequestURI().equals(pattern)) {
            return false;
        }
        if (!request.getMethod().equals(method.name())) {
            return false;
        }
        final Set<String> requestParameters = getParameterNamesAsSet(request);
        return requestParameters.equals(parameters);
    }

    private Set<String> getParameterNamesAsSet(final HttpServletRequest request) {
        final Enumeration<String> parameterNames = request.getParameterNames();
        return parameterNames != null ?
                Collections.list(parameterNames).stream().collect(Collectors.toSet()) :
                Collections.emptySet();
    }

}
