package ododock.webserver.service;

import lombok.RequiredArgsConstructor;
import ododock.webserver.request.Login;
import ododock.webserver.security.JwtToken;
import ododock.webserver.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtToken login(final Login request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtToken token = jwtTokenProvider.generateToken(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

}
