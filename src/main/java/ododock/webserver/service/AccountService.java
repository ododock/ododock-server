package ododock.webserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.Authority;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.exception.ResourceAlreadyExistsException;
import ododock.webserver.exception.ResourceNotFoundException;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.repository.AuthoritiesRepository;
import ododock.webserver.repository.ProfileRepository;
import ododock.webserver.request.AccountCreate;
import ododock.webserver.request.AccountPasswordUpdate;
import ododock.webserver.response.AccountCreateResponse;
import ododock.webserver.response.AccountDetailsResponse;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final AuthoritiesRepository authoritiesRepository;
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtToken login(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        return jwtToken;
    }

    @Transactional(readOnly = true)
    public AccountDetailsResponse getAccount(final Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        return AccountDetailsResponse.of(account);
    }

    @Transactional
    public AccountCreateResponse createAccount(final AccountCreate request) {
        if (validateEmail(request.email())) {
            throw new ResourceAlreadyExistsException(Account.class, request.email());
        };
        if (validateUsername(request.username())) {
            throw new ResourceAlreadyExistsException(Account.class, request.username());
        }
        if (profileRepository.existsByNickname(request.nickname())) {
            throw new ResourceAlreadyExistsException(Profile.class, request.nickname());
        }
        Account newAccount = Account.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .birthDate(request.birthDate())
                .nickname(request.nickname()) // profile repository
                .fullname(request.fullname())
                .imageSource(request.imageSource())
                .fileType(request.fileType())
                .build();
        Account createdAccount = accountRepository.save(newAccount);

//        Authority authority = new Authority(
//                createdAccount.getId(),
//                "ROLE_USER");
//        authoritiesRepository.save(authority);
        return AccountCreateResponse.builder()
                .accountId(createdAccount.getId())
                .profileId(createdAccount.getOwnProfile().getId())
                .build();
    }

    @Transactional
    public void updateAccountPassword(final Long accountId, final AccountPasswordUpdate request) {
        Account account = accountRepository.findById(accountId)
                        .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        account.updatePassword(passwordEncoder.encode(request.password()));
    }

    @Transactional
    public void deleteAccount(final Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        accountRepository.delete(account);
    }

    @Transactional(readOnly = true)
    public boolean validateUsername(final String username) {
        return accountRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean validateEmail(final String email) {
        return accountRepository.existsByEmail(email);
    }

    private Collection<GrantedAuthority> getAuthorities(Long userId) {
        List<Authority> authList = authoritiesRepository.findByUserId(userId);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Authority authority : authList) {
            authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return authorities;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account foundMember = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, email));
        List<GrantedAuthority> roles = new ArrayList<>();
        AccountContext memberContext = new AccountContext(foundMember, roles);
        return memberContext;
    }
}
