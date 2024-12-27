package ododock.webserver.domain.notification;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.verification.VerificationInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GMailService implements MailService {

    private final JavaMailSender emailSender;
    private final String VERIFICATION_MAIL_TITLE = "오도독 회원가입 이메일 인증코드";
    private final String VERIFICATION_MAIL_CONTENT = "오도독 회원가입 인증코드: %s";

    @Value("${spring.mail.username}")
    private String sender;

    public void sendVerificationCode(final VerificationInfo verificationInfo) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(verificationInfo.getTargetEmail());
        message.setSubject(VERIFICATION_MAIL_TITLE);
        message.setText(String.format(VERIFICATION_MAIL_CONTENT, verificationInfo.getCode()));
        emailSender.send(message);
    }

}
