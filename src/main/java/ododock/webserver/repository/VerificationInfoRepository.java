package ododock.webserver.repository;

import ododock.webserver.domain.account.VerificationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationInfoRepository extends JpaRepository<VerificationInfo, Long> {

    VerificationInfo save(VerificationInfo verificationInfo);

    boolean existsByTargetEmail(String targetEmail);

    Optional<VerificationInfo> findByTargetEmail(String targetEmail);

}
