package ododock.webserver.security;

import lombok.extern.slf4j.Slf4j;
import ododock.webserver.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * FormLogin방식을 현재 사용하지 않아서 Provider를 사용하지않고,
 * JwtAuthenticationFilter를 활용하는것으로 보임.
 */
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("CustomAuthenticationProvider authenticate() executed");
        // 파라미터 Authentication은 AuthenticationManager로부터 전달받은 것으로,
        // 사용자가 입력한 ID/PW 등 로그인 정보가 담겨있음

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        AccountContext userContext = (AccountContext) accountService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userContext.getPassword())) {
            throw new BadCredentialsException("password incorrect");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userContext.getUsername(), null, userContext.getAuthorities());

        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        log.info("CustomAuthenticationProvider supports() executed");
        log.info(authentication.toString());
        log.info(String.valueOf(UsernamePasswordAuthenticationFilter.class.isAssignableFrom(authentication)));
        return UsernamePasswordAuthenticationFilter.class.isAssignableFrom(authentication);
    }
}
