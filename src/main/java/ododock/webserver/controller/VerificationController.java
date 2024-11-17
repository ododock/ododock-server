package ododock.webserver.controller;

import lombok.RequiredArgsConstructor;
import ododock.webserver.service.VerificationService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class VerificationController {

    private final VerificationService verificationService;

    @PutMapping("/api/v1/verifications")
    public void issueVerificationCode(final String email) throws Exception {
        verificationService.sendEmailVerificationCode(email);
    }

}
