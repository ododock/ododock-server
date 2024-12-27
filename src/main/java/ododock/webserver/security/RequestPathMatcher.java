package ododock.webserver.security;

import ododock.webserver.security.util.RequestParameterMatcher;
import ododock.webserver.web.ResourcePath;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

public final class RequestPathMatcher {

    public static final RequestMatcher PERMIT_ALL_MATCHER;
    public static final RequestMatcher AUTHENTICATED_MATCHER;

    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String PATCH = "PATCH";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    static {
        PERMIT_ALL_MATCHER = new OrRequestMatcher(
                new AntPathRequestMatcher(ResourcePath.DOCS + "/**", GET),

                new AntPathRequestMatcher(ResourcePath.OAUTH2 + "/authorization" + "/**"),
                new AntPathRequestMatcher(ResourcePath.OAUTH_CALLBACK),
                new AntPathRequestMatcher(ResourcePath.AUTH_PROCESSING_URL, "POST"),
                new AntPathRequestMatcher(ResourcePath.AUTH_LOGOUT_URL, "POST"),

                new AntPathRequestMatcher(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.VERIFICATION + "/*", POST), // verification-code 발급

                new RequestParameterMatcher(HttpMethod.GET, ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS, List.of("email")),
                new RequestParameterMatcher(HttpMethod.GET, ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS, List.of("nickname")),

                new AntPathRequestMatcher(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS, POST),
                new AntPathRequestMatcher(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS + ResourcePath.VERIFICATION, POST),
                new AntPathRequestMatcher(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS + "/{" + ResourcePath.PATH_VAR_ID + "}", GET),
                new AntPathRequestMatcher(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS + "/{" + ResourcePath.PATH_VAR_NAME + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PASSWORD, PUT)
        );
        AUTHENTICATED_MATCHER = new OrRequestMatcher(
                new AntPathRequestMatcher(ResourcePath.API + "/**")
        );
    }

    private RequestPathMatcher() {
    }

}
