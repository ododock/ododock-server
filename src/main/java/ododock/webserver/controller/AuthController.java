package ododock.webserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<?> logout(final HttpServletRequest request) {
        return ResponseEntity.ok().build();
    }

}
