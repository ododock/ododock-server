package ododock.webserver.web.v1alpha1;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.verification.VerificationService;
import ododock.webserver.web.ResourcePath;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.VERIFICATION)
@RestController
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping(
            value = ResourcePath.ACCOUNTS
    )
    public void issueVerificationCode(
            final String email
    ) throws Exception {
        verificationService.issueVerificationCode(email);
    }

}
