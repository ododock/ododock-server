package ododock.webserver.repository;

import ododock.webserver.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(final String email);
    boolean existsBySocialAccountsEmail(final String email);
    Optional<Account> findByEmail(String email);
    Optional<Account> findBySocialAccountsEmail(String email);

}
