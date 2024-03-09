package ododock.webserver.repository;

import ododock.webserver.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByUsername(final String username);
    boolean existsByEmail(final String email);
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);

}
