package ododock.webserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.Role;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.exception.ResourceAlreadyExistsException;
import ododock.webserver.exception.ResourceNotFoundException;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.repository.ProfileRepository;
import ododock.webserver.repository.RoleRepository;
import ododock.webserver.request.AccountCreate;
import ododock.webserver.request.AccountPasswordUpdate;
import ododock.webserver.response.AccountCreateResponse;
import ododock.webserver.response.AccountDetailsResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;

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
        if (profileRepository.existsByNickname(request.nickname())) {
            throw new ResourceAlreadyExistsException(Profile.class, request.nickname());
        }
        Account newAccount = Account.builder()
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .birthDate(request.birthDate())
                .nickname(request.nickname()) // profile
                .fullname(request.fullname())
                .imageSource(request.imageSource())
                .fileType(request.fileType())
                .build();
        Account createdAccount = accountRepository.save(newAccount);

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
    public boolean validateEmail(final String email) {
        return accountRepository.existsByEmail(email);
    }

    private Collection<GrantedAuthority> getAuthorities(final Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        List<Role> roles = account.getRoles();
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

}
