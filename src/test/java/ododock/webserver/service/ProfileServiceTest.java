package ododock.webserver.service;

import jakarta.persistence.EntityManager;
import ododock.webserver.common.CleanUp;
import ododock.webserver.domain.account.*;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.web.v1alpha1.dto.response.ProfileDetailsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@SpringBootTest
//@Transactional
public class ProfileServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private CleanUp cleanup;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

//    @Test
    @Transactional
    void validate_nickname() {
        // given
        final Account account = Account.builder()
                .nickname("test-user")
                .email("test-user@oddk.xyz")
                .password("password")
                .fullname("John Doe")
                .birthDate(LocalDate.of(1991, 5, 22))
                .roles(Set.of(Role.USER))
                .build();
        accountRepository.save(account);

        // when
        Optional<Account> accountOpt = accountRepository.findByOwnProfile_Nickname("test-user");
        boolean result = profileService.isAvailableNickname("test-user");

        // then
        assertThat(result).isFalse();
    }

//    @Test
    @Transactional
    void get_profile() {
        // given
        final Account account = Account.builder()
                .nickname("test-user")
                .email("test-user@oddk.xyz")
                .password("password")
                .fullname("John Doe")
                .birthDate(LocalDate.of(1991, 5, 22))
                .roles(Set.of(Role.USER))
                .build();
        Account createdAccount = accountRepository.save(account);
        Profile createdProfile = createdAccount.getOwnProfile();
        em.flush();
        createdProfile.updateProfileImage("http://test.com/temp.png", "png");
        em.flush();

        // when
        ProfileDetailsResponse result = profileService.getProfile(createdAccount.getId());

        // then
        assertThat(result.nickname()).isEqualTo("test-user");
        assertThat(result.profileImage().getImageSource()).isEqualTo("http://test.com/temp.png");
        assertThat(result.profileImage().getFileType()).isEqualTo("png");

    }

}
