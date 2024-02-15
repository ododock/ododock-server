package ododock.webserver.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.AccountDto;
import ododock.webserver.domain.account.Authority;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.repository.AuthoritiesRepository;
import ododock.webserver.security.AccountContext;
import ododock.webserver.security.JwtToken;
import ododock.webserver.security.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final AuthoritiesRepository authoritiesRepository;
    private final AccountRepository accountRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("UserDetailsService's loadUserByUsername executed");
        Account foundMember = accountRepository.findByEmail(email);

        if (foundMember == null) {
            log.info(email + " not found");
            throw new UsernameNotFoundException(email + "not exists");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        AccountContext memberContext = new AccountContext(foundMember, roles);

        return memberContext;
    }

    public JwtToken login(String email, String password) {
        log.info("CustomUserDetailsService's login() executed");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        // UsernamePasswordAuthenticationToken은 request DTO의 정보를 전달해서 생성함
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        return jwtToken;
    }

    // TODO need to implement user authority related things
    public Account signup(AccountDto memberDto) {
        log.info("CustomUserDetailsService executed");

        Account newAccount = Account.builder()
                .username(memberDto.getUsername())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .email(memberDto.getEmail())
                .build();
        Account registeredAccount = accountRepository.save(newAccount);
        Authority authority = new Authority(
                registeredAccount.getId(),
                "ROLE_USER");
        authoritiesRepository.save(authority);
        log.info(registeredAccount.getUsername() + " has been registered");
        return registeredAccount;
    }

    public Account findUserByUsername(String username) {
        Account foundUser = accountRepository.findByUsername(username);
        if (foundUser == null) {
            log.info(username + " user not found");
            return null;
        }
        return foundUser;
    }

    // TODO need to implement authority populate
    private Collection<GrantedAuthority> getAuthorities(Long userId) {
        List<Authority> authList = authoritiesRepository.findByUserId(userId);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Authority authority : authList) {
            authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return authorities;
    }
}
