package ododock.webserver.service;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.account.VerificationInfo;
import ododock.webserver.exception.InvalidVerificationCodeException;
import ododock.webserver.exception.ResourceNotFoundException;
import ododock.webserver.exception.VerificationCodeExpiredException;
import ododock.webserver.repository.VerificationInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationInfoRepository verificationInfoRepository;
    private final MailService mailService;

    @Transactional(readOnly = true)
    public boolean existsByTargetEmail(final String targetEmail) {
        return verificationInfoRepository.existsByTargetEmail(targetEmail);
    }

    @Transactional
    public VerificationInfo issueVerificationCode(final String email) {
        final VerificationInfo verificationInfo = new VerificationInfo(email);
        return verificationInfoRepository.save(verificationInfo);
    }

    @Transactional(readOnly = true)
    public boolean verifyCode(final String email, final String code) throws InvalidVerificationCodeException, VerificationCodeExpiredException {
        final VerificationInfo verificationInfo = verificationInfoRepository.findByTargetEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(VerificationInfo.class, email));
        return verificationInfo.validate(code);
    }

    @Transactional
    public void sendEmailVerificationCode(final String email) throws Exception {
        final Optional<VerificationInfo> verificationInfoOpt = verificationInfoRepository.findByTargetEmail(email);
        if (verificationInfoOpt.isEmpty()) {
            verificationInfoRepository.save(new VerificationInfo(email));
        } else {
            mailService.sendVerificationCode(email, verificationInfoOpt.get().getCode());
        }
    }

}
