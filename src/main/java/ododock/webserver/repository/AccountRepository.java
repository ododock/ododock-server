package ododock.webserver.repository;

import ododock.webserver.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(final String email);

    boolean existsByOwnProfile_Nickname(final String nickname);

    boolean existsByOwnProfile_Fullname(final String fullname);

    boolean existsBySocialAccounts_Email(final String email);

    Optional<Account> findByEmail(final String email);

    Optional<Account> findByOwnProfile_Nickname(final String nickname);

    Optional<Account> findAccountWithRolesBySocialAccountsProviderId(final String providerId);

}
