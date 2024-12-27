package ododock.webserver.domain.verification;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.notification.MailService;
import ododock.webserver.web.exception.InvalidVerificationCodeException;
import ododock.webserver.web.exception.ResourceNotFoundException;
import ododock.webserver.web.exception.VerificationCodeExpiredException;
import ododock.webserver.repository.VerificationInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationInfoRepository verificationInfoRepository;
    private final MailService mailService;

    @Transactional
    public void issueVerificationCode(final String email) throws Exception {
        VerificationInfo verificationInfo = verificationInfoRepository.findByTargetEmail(email).orElse(new VerificationInfo(email));
        verificationInfoRepository.save(verificationInfo);
        mailService.sendVerificationCode(verificationInfo);
    }

    @Transactional(readOnly = true)
    public boolean verifyCode(final String email, final String code) throws InvalidVerificationCodeException, VerificationCodeExpiredException {
        final VerificationInfo verificationInfo = verificationInfoRepository.findByTargetEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(VerificationInfo.class, email));
        return verificationInfo.validate(code);
    }

}
