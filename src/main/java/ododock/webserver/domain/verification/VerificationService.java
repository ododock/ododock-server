package ododock.webserver.domain.verification;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.notification.MailService;
import ododock.webserver.repository.jpa.VerificationInfoRepository;
import ododock.webserver.web.VerificationCodeException;
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
    public boolean verifyCode(final String email, final String code) throws VerificationCodeException {
        final VerificationInfo verificationInfo = verificationInfoRepository.findByTargetEmail(email)
                .orElseThrow(() -> new VerificationCodeException("Invalid verification code"));
        return verificationInfo.validate(code);
    }

}
