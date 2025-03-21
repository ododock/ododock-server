package ododock.webserver.domain.verification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ododock.webserver.web.VerificationCodeException;
import ododock.webserver.util.CodeGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
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

    private boolean isExpired() throws VerificationCodeException {
        if (!LocalDateTime.now().isBefore(expiryTimestamp)) {
            throw new VerificationCodeException("Verification code expired");
        }
        return false;
    }

    private boolean isValidCode(final String code) throws VerificationCodeException {
        if (!this.code.equals(code)) {
            throw new VerificationCodeException("Invalid verification code");
        }
        return this.code.equals(code);
    }

    public VerificationInfo(String targetEmail) {
        this.targetEmail = targetEmail;
        generateVerificationCode();
    }

    public boolean validate(final String code) throws VerificationCodeException {
        return !isExpired() && isValidCode(code);
    }

    private void generateVerificationCode() {
        this.code = CodeGenerator.generateCode(CODE_LENGTH);
        this.creationTimestamp = LocalDateTime.now();
        this.expiryTimestamp = LocalDateTime.now().plusMinutes(30);
    }

}
