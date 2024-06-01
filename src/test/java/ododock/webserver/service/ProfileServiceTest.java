package ododock.webserver.service;

import jakarta.persistence.EntityManager;
import ododock.webserver.common.CleanUp;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.Role;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.repository.ProfileRepository;
import ododock.webserver.response.ProfileDetailsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
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

    @Test
    void validate_nickname() {
        // given
        final Account account = Account.builder()
                .nickname("test-user")
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

    @Test
    void get_profile() {
        // given
        final Account account = Account.builder()
                .nickname("test-user")
                .email("test-user@ododock.io")
                .password("password")
                .fullname("John Doe")
                .birthDate(LocalDate.of(1991, 5, 22))
                .roles(Set.of(Role.USER))
                .build();
        Account save = accountRepository.save(account);
        System.out.println(save.getId());
        System.out.println(save.getOwnProfile().getId());


        // when
        ProfileDetailsResponse result = profileService.getProfile(1L);

        // then
        assertThat(result.nickname()).isEqualTo("test-user");
        assertThat(result.imageSource()).isEqualTo("http://storage.ododock.io/sample.png");
        assertThat(result.fileType()).isEqualTo("png");
    }


    @Test
    void update_profile() {
        System.out.println("ROLE NAME");
        System.out.println(Role.USER);
        System.out.println(Role.USER.getRoleName());
    }

    @Test
    void update_profile_image() {

    }
}
