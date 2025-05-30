package ododock.webserver.service;

import ododock.webserver.common.CleanUp;
import ododock.webserver.domain.StorageService;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.AccountQueryService;
import ododock.webserver.domain.account.Role;
import ododock.webserver.domain.profile.ProfileService;
import ododock.webserver.repository.jpa.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class AccountQueryServiceTest {

    @Autowired
    private AccountQueryService accountQueryService;

    @Autowired
    private AccountRepository accountRepository;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private StorageService storageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CleanUp cleanUp;

    @Test
    @Transactional
    void getAccount() {
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
        Account result = accountQueryService.getAccountDetails(id);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getEmail()).isEqualTo("test-user@oddk.xyz");
        assertThat(result.getOwnProfile().getFullname()).isEqualTo("John Doe");
        assertThat(result.getOwnProfile().getBirthDate()).isEqualTo("1991-05-22");
    }

}
