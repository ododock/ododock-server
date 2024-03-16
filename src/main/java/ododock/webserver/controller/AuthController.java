package ododock.webserver.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ododock.webserver.request.Login;
import ododock.webserver.security.JwtToken;
import ododock.webserver.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/api/v1/auth/login")
    public JwtToken signin(@Valid @RequestBody Login request) {
        return null;
    }

//    @PostMapping("/api/v1/auth/logout")
//    public ResponseEntity<Void> logout(final HttpServletRequest request, final HttpServletResponse response) {
//        authService.logout(request, response);
//        return ResponseEntity.ok().build();
//    }

}
