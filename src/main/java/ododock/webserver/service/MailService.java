package ododock.webserver.service;

public interface MailService {

    public void sendVerificationCode(String userMail, String verificationCode) throws Exception;

}
