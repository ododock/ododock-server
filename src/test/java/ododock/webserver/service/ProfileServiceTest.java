package ododock.webserver.service;

import jakarta.persistence.EntityManager;
import ododock.webserver.common.CleanUp;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.Role;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.domain.profile.ProfileService;
import ododock.webserver.repository.jpa.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private AccountRepository accountRepository;

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
        Profile result = profileService.getProfile(createdAccount.getId());

        // then
        assertThat(result.getNickname()).isEqualTo("test-user");
        assertThat(result.getProfileImage().getSourcePath()).isEqualTo("http://test.com/temp.png");
        assertThat(result.getProfileImage().getFileType()).isEqualTo("png");

    }

}
