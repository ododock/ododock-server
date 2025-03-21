package ododock.webserver.domain.account;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.verification.VerificationService;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.web.ResourceNotFoundException;
import ododock.webserver.web.VerificationCodeException;
import ododock.webserver.web.v1alpha1.dto.account.AccountPasswordReset;
import ododock.webserver.web.v1alpha1.dto.account.CompleteDaoAccountVerification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountManageService {

    private final VerificationService verificationService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void verifyDaoAccountEmail(final CompleteDaoAccountVerification request) throws VerificationCodeException {
        Account foundAccount = accountRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, "email"));
        this.verificationService.verifyCode(request.email(), request.verificationCode());
        foundAccount.verifyEmail();
        foundAccount.activate();
    }

    @Transactional
    public void resetAccountPassword(final String name, final AccountPasswordReset request) throws VerificationCodeException {
        final Account foundAccount = accountRepository.findByEmail(name)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, name));
        if (this.verificationService.verifyCode(foundAccount.getEmail(), request.verificationCode())) {
            foundAccount.updatePassword(passwordEncoder.encode(request.newPassword()));
        }
    }

}
