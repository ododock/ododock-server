package ododock.webserver.domain.account;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.verification.VerificationService;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.web.ResourceNotFoundException;
import ododock.webserver.web.VerificationCodeException;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Account;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class AccountManageService {

    private final VerificationService verificationService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void verifyDaoAccountEmail(final V1alpha1Account request) throws VerificationCodeException {
        Assert.notNull(request.getEmail(), "Email is required");
        Assert.notNull(request.getVerificationCode(), "VerificationCode is required");
        Account foundAccount = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, "email"));
        this.verificationService.verifyCode(request.getEmail(), request.getVerificationCode());
        foundAccount.verifyEmail();
        foundAccount.activate();
    }

    @Transactional
    public void resetAccountPassword(final String name, final V1alpha1Account request) throws VerificationCodeException {
        Assert.notNull(name, "Name is required");
        Assert.notNull(request.getEmail(), "Email is required");
        Assert.notNull(request.getVerificationCode(), "VerificationCode is required");
        final Account foundAccount = accountRepository.findByEmail(name)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, name));
        if (this.verificationService.verifyCode(foundAccount.getEmail(), request.getVerificationCode())) {
            foundAccount.updatePassword(passwordEncoder.encode(request.getPassword()));
        }
    }

}
