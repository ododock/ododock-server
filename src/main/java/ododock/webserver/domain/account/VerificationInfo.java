package ododock.webserver.domain.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.exception.InvalidVerificationCodeException;
import ododock.webserver.exception.VerificationCodeExpiredException;
import ododock.webserver.util.CodeGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationInfo {

    private final int CODE_LENGTH = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "target_email")
    private String targetEmail;

    @NotBlank
    @Column(name = "verification_code")
    private String code;

    @Column(name = "code_creation_timestamp")
    private LocalDateTime creationTimestamp;

    @Column(name = "code_expiry_timestamp")
    private LocalDateTime expiryTimestamp;

    private boolean isExpired() throws VerificationCodeExpiredException {
        if (!LocalDateTime.now().isBefore(expiryTimestamp)) {
            throw new VerificationCodeExpiredException("verification verificationCode is expired");
        }
        return false;
    }

    private boolean isValidCode(final String code) throws InvalidVerificationCodeException {
        if (!this.code.equals(code)) {
            throw new InvalidVerificationCodeException("verification verificationCode is invalid");
        }
        return this.code.equals(code);
    }

    public VerificationInfo(String targetEmail) {
        this.targetEmail = targetEmail;
        generateVerificationCode();
    }

    public boolean validate(final String code) throws VerificationCodeExpiredException, InvalidVerificationCodeException {
        return !isExpired() && isValidCode(code);
    }

    public VerificationInfo generateVerificationCode() {
        this.code = CodeGenerator.generateCode(CODE_LENGTH);
        this.creationTimestamp = LocalDateTime.now();
        this.expiryTimestamp = LocalDateTime.now().plusMinutes(30);
        return this;
    }

}
