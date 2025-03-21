package ododock.webserver.service;

import jakarta.persistence.EntityManager;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.AccountManageService;
import ododock.webserver.domain.account.AccountService;
import ododock.webserver.domain.account.Role;
import ododock.webserver.domain.notification.GoogleMailService;
import ododock.webserver.domain.verification.VerificationInfo;
import ododock.webserver.domain.verification.VerificationService;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.repository.jpa.VerificationInfoRepository;
import ododock.webserver.web.v1alpha1.dto.account.AccountPasswordReset;
import ododock.webserver.web.v1alpha1.dto.account.AccountPasswordUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

//@Sql(scripts = "classpath:db/teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//@SpringBootTest
//@Transactional
public class AccountServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private VerificationInfoRepository verificationInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private GoogleMailService mailService;

    @Autowired
    private AccountManageService accountManageService;

//    @Test
    @Transactional
    void isAvailableEmail() {
        // given
        final Account account = Account.builder()
                .nickname("test-user")
                .email("test-user@oddk.xyz")
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

//    @Test
    @Transactional
    void createAccount() throws Exception {
        // given
        final Account request = Account.builder()
                .nickname("test-user")
                .email("test-user@oddk.xyz")
                .password("password")
                .fullname("John Doe")
                .birthDate(LocalDate.of(1991, 5, 22))
                .build();

        // when
        accountService.createDaoAccount(request);

        // then
        Optional<Account> account = accountRepository.findByEmail("test-user@oddk.xyz");

        assertThat(account.isPresent()).isTrue();
        assertThat(account.get().getId()).isNotNull();
        assertThat(account.get().getOwnProfile().getNickname()).isEqualTo("test-user");
        assertThat(account.get().getOwnProfile().getFullname()).isEqualTo("John Doe");
    }

//    @Test
    @Transactional
    void updateAccountPassword() {
        // given
        final Account account = Account.builder()
                .nickname("test-user")
                .email("test-user@oddk.xyz")
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
        Optional<Account> found = accountRepository.findByEmail("test-user@oddk.xyz");
        assertThat(found.isPresent()).isTrue();
        assertThat(passwordEncoder.matches("123456", found.get().getPassword())).isTrue();
    }


//    @Test
    @Transactional
    void resetAccountPassword() throws Exception {
        // given
        final Account createRequest = Account.builder()
                .nickname("john-doe")
                .email("test-user@oddk.xyz")
                .password("password")
                .fullname("John Doe")
                .birthDate(LocalDate.of(1991, 5, 22))
                .build();

        accountService.createDaoAccount(createRequest);
        Account account = accountRepository.findByEmail(createRequest.getEmail()).orElseThrow(IllegalStateException::new);

        verificationService.issueVerificationCode(createRequest.getEmail());
        VerificationInfo verificationInfo = verificationInfoRepository.findByTargetEmail(account.getEmail())
                .orElseThrow(IllegalStateException::new);

        AccountPasswordReset resetRequest = AccountPasswordReset.builder()
                .newPassword("newPassword")
                .verificationCode(verificationInfo.getCode())
                .build();

        // when
        accountManageService.resetAccountPassword(createRequest.getEmail(), resetRequest);

        // then
        Optional<Account> found = accountRepository.findByEmail("test-user@oddk.xyz");
        assertThat(found.isPresent()).isTrue();
        assertThat(passwordEncoder.matches("newPassword", found.get().getPassword())).isTrue();
    }

//    @Test
    @Transactional
    void deleteAccount() {
        // given
        final Account account = Account.builder()
                .nickname("test-user")
                .email("test-user@oddk.xyz")
                .password(passwordEncoder.encode("password"))
                .fullname("John Doe")
                .birthDate(LocalDate.of(1991, 5, 22))
                .roles(Set.of(Role.USER))
                .build();
        Long id = accountRepository.save(account).getId();

        // when
        accountService.deleteAccount(id);

        // then
        Optional<Account> found = accountRepository.findByEmail("test-user@oddk.xyz");
        assertThat(found).isEmpty();
    }

}
