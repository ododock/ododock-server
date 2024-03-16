package ododock.webserver.service;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.account.Account;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email " + email + " not found"));
        return new CustomUserDetails(account);
    }

}
