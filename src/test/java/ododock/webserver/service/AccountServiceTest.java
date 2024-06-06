package ododock.webserver.service;

import jakarta.persistence.EntityManager;
import ododock.webserver.common.CleanUp;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.Role;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.repository.ProfileRepository;
import ododock.webserver.request.account.AccountCreate;
import ododock.webserver.request.account.AccountPasswordUpdate;
import ododock.webserver.response.account.AccountCreateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

//@Sql(scripts = "classpath:db/teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest
@Transactional
public class AccountServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CleanUp cleanUp;

    @Test
    @Transactional
    void isAvailableEmail() {
        // given
        final Account account = Account.builder()
                .nickname("test-user")
                .email("test-user@ododock.io")
                .password(passwordEncoder.encode("password"))
                .fullname("John Doe")
                .birthDate(LocalDate.of(1991, 5, 22))
                .roles(Set.of(Role.USER))
                .build();
        accountRepository.save(account);
        final String request = "test@ododock.io";

        // when
        boolean result = accountService.isAvailableEmail(request);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @Transactional
    void createAccount() {
        // given
        final AccountCreate request = AccountCreate.builder()
                .nickname("test-user")
                .email("test-user@ododock.io")
                .password("password")
                .fullname("John Doe")
                .birthDate(LocalDate.of(1991, 5, 22))
                .build();

        // when
        AccountCreateResponse result = accountService.createDaoAccount(request);


        // then
        Optional<Account> account = accountRepository.findByEmail("test-user@ododock.io");
        Optional<Profile> profile = profileRepository.findByNickname("test-user");

        assertThat(account.isPresent()).isTrue();
        assertThat(result.sub()).isEqualTo(account.get().getId());
        assertThat(profile.isPresent()).isTrue();
        assertThat(result.profileId()).isEqualTo(profile.get().getId());
    }

    @Test
    @Transactional
    void updateAccountPassword() {
        // given
        final Account account = Account.builder()
                .nickname("test-user")
                .email("test-user@ododock.io")
                .password(passwordEncoder.encode("password"))
                .fullname("John Doe")
                .birthDate(LocalDate.of(1991, 5, 22))
                .roles(Set.of(Role.USER))
                .build();
        Long id = accountRepository.save(account).getId();
        AccountPasswordUpdate request = AccountPasswordUpdate.builder()
                .password("123456")
                .build();

        // when
        accountService.updateAccountPassword(id, request);

        // then
        Optional<Account> found = accountRepository.findByEmail("test-user@ododock.io");
        assertThat(found.isPresent()).isTrue();
        assertThat(passwordEncoder.matches("123456", found.get().getPassword())).isTrue();
    }

    @Test
    @Transactional
    void deleteAccount() {
        // given
        final Account account = Account.builder()
                .nickname("test-user")
                .email("test-user@ododock.io")
                .password(passwordEncoder.encode("password"))
                .fullname("John Doe")
                .birthDate(LocalDate.of(1991, 5, 22))
                .roles(Set.of(Role.USER))
                .build();
        Long id = accountRepository.save(account).getId();

        // when
        accountService.deleteAccount(id);

        // then
        Optional<Account> found = accountRepository.findByEmail("test-user@ododock.io");
        assertThat(found).isEmpty();
    }

}
