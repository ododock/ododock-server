package ododock.webserver.service;

import jakarta.persistence.EntityManager;
import ododock.webserver.common.CleanUp;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.Role;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.repository.ProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProfileServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private CleanUp cleanup;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    void tearDown() {
        cleanup.all();
    }

    @Test
    void validate_nickname() {
        // given
        final Account account = Account.builder()
                .nickname("test-user")
                .imageSource("http://storage.ododock.io/sample.png")
                .fileType("png")
                .email("test-user@ododock.io")
                .password("password")
                .fullname("John Doe")
                .birthDate(LocalDate.of(1991, 5, 22))
                .roles(Set.of(Role.USER))
                .build();
        accountRepository.save(account);

        // when
        Optional<Profile> profile = profileRepository.findByNickname("test-user");
        boolean result = profileService.isAvailableNickname("test-user");

        System.out.println(profile.isPresent());
        System.out.println(profile.get().getNickname());

        // then
        assertThat(result).isFalse();
    }

}
