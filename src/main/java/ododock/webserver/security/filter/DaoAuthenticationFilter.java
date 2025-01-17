package ododock.webserver.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ododock.webserver.security.handler.MainAuthenticationFailureHandler;
import ododock.webserver.security.request.LoginRequest;
import ododock.webserver.security.handler.DaoAuthenticationSuccessHandler;
import ododock.webserver.web.ResourcePath;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class DaoAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public DaoAuthenticationFilter(AuthenticationManager authenticationManager, DaoAuthenticationSuccessHandler successHandler) {
        this.authenticationManager = authenticationManager;
        this.setFilterProcessesUrl(ResourcePath.AUTH_PROCESSING_URL);
        this.setAuthenticationSuccessHandler(successHandler);
        this.setAuthenticationFailureHandler(new MainAuthenticationFailureHandler());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            UsernamePasswordAuthenticationToken authRequest =
                    UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.email(), loginRequest.password());
            return authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse login request", e);
        }
    }

}
