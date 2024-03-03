package ododock.webserver.repository;

import ododock.webserver.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);

}
