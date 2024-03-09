package ododock.webserver.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ododock.webserver.request.Login;
import ododock.webserver.security.JwtToken;
import ododock.webserver.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("api/v1/auth/login")
    public JwtToken signin(@Valid @RequestBody Login request) {
        return authService.login(request);
    }

}
