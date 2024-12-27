package ododock.webserver.domain.notification;

import ododock.webserver.domain.verification.VerificationInfo;

public interface MailService {

    void sendVerificationCode(VerificationInfo verificationInfo) throws Exception;

}
