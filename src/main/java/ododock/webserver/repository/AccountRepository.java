package ododock.webserver.repository;

import ododock.webserver.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    public Account findByUsername(String username);

    public Account findByEmail(String email);

}
