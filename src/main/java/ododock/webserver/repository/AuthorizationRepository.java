package ododock.webserver.repository;

import ododock.webserver.domain.account.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizationRepository extends JpaRepository<Authorization, Long> {

    Boolean existsByUsername(final String username);

    Boolean existsByRefreshTokenValue(final String token);

    void deleteByRefreshTokenValue(final String token);

    Optional<Authorization> findByUsername(final String username);

    Optional<Authorization> findByRefreshTokenValue(final String refreshToken);

}
