package ododock.webserver.repository;

import ododock.webserver.domain.account.TokenRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRecordRepository extends JpaRepository<TokenRecord, Long> {

    Boolean existsByAccountId(final Long accountId);

    Boolean existsByRefreshTokenValue(final String token);

    void deleteByRefreshTokenValue(final String token);

    void deleteByAccountId(final Long accountId);

    void deleteAllByAccountId(final Long accountId);

    List<TokenRecord> findByAccountId(final Long accountId);

    Optional<TokenRecord> findByRefreshTokenValue(final String refreshToken);

}
