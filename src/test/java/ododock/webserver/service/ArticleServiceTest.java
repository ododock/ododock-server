package ododock.webserver.service;

import jakarta.persistence.EntityManager;
import ododock.webserver.common.CleanUp;
import ododock.webserver.domain.account.AccountService;
import ododock.webserver.domain.account.ProfileService;
import ododock.webserver.repository.jpa.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ArticleServiceTest {

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

}
