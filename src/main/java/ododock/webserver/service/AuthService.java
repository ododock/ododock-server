package ododock.webserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.Account;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.security.service.DaoUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        log.info("Auth Service find user by email " + email);
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "is not found"));
        DaoUserDetails daoUserDetails = new DaoUserDetails(account);
        return daoUserDetails;
    }

}
