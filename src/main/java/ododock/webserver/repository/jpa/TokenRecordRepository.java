package ododock.webserver.repository.jpa;

import ododock.webserver.security.TokenRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRecordRepository extends JpaRepository<TokenRecord, Long> {

    Boolean existsByAccountId(final String accountId);

    Boolean existsByRefreshTokenValue(final String token);

    void deleteByRefreshTokenValue(final String token);

    void deleteByAccountId(final String accountId);

    void deleteAllByAccountId(final String accountId);

    List<TokenRecord> findByAccountId(final String accountId);

    Optional<TokenRecord> findByRefreshTokenValue(final String refreshToken);

}
