package ododock.webserver.domain.account;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import ododock.webserver.exception.InvalidVerificationCodeException;
import ododock.webserver.exception.VerificationCodeExpiredException;
import ododock.webserver.util.CodeGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Embeddable
@NoArgsConstructor
public class VerificationInfo {

    private final int CODE_LENGTH = 4;

    @NotBlank
    @Column(name = "verification_code")
    private String code;

    @Column(name = "code_creation_timestamp")
    private LocalDateTime creationTimestamp;

    @Column(name = "code_expiry_timestamp")
    private LocalDateTime expiryTimestamp;

    private boolean isExpired() throws VerificationCodeExpiredException {
        if (!LocalDateTime.now().isBefore(expiryTimestamp)) {
            throw new VerificationCodeExpiredException("verification code is expired");
        }
        return false;
    }

    private boolean isValidCode(final String code) throws InvalidVerificationCodeException {
        if (!this.code.equals(code)) {
            throw new InvalidVerificationCodeException("verification code is invalid");
        }
        return this.code.equals(code);
    }

    public boolean validate(final String code) throws VerificationCodeExpiredException, InvalidVerificationCodeException {
        return !isExpired() && isValidCode(code);
    }

    public VerificationInfo generatePasswordResetCode() {
        this.code = UUID.randomUUID().toString();
        this.creationTimestamp = LocalDateTime.now();
        this.expiryTimestamp = LocalDateTime.now().plusMinutes(10);
        return this;
    }

    public VerificationInfo generateVerificationCode() {
        this.code = CodeGenerator.generateCode(CODE_LENGTH);
        this.creationTimestamp = LocalDateTime.now();
        this.expiryTimestamp = LocalDateTime.now().plusMinutes(30);
        return this;
    }

}
