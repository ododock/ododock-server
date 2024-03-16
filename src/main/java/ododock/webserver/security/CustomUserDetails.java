package ododock.webserver.security;

import ododock.webserver.domain.account.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final Account account;

    public CustomUserDetails(final Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return account.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.account.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return account.getCredentialNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return account.getEnabled();
    }
}
