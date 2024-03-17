package ododock.webserver.repository;

import ododock.webserver.domain.account.TokenRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRecordRepository extends JpaRepository<TokenRecord, Long> {

    Boolean existsByUsername(final String username);

    Boolean existsByRefreshTokenValue(final String token);

    void deleteByRefreshTokenValue(final String token);

    Optional<TokenRecord> findByUsername(final String username);

    Optional<TokenRecord> findByRefreshTokenValue(final String refreshToken);

}
