package swim.webserver.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import swim.webserver.domain.Authority;
import swim.webserver.domain.Member;
import swim.webserver.domain.MemberContext;
import swim.webserver.domain.MemberDto;
import swim.webserver.repository.AuthoritiesRepository;
import swim.webserver.repository.MemberRepository;
import swim.webserver.security.JwtToken;
import swim.webserver.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
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

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final AuthoritiesRepository authoritiesRepository;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("UserDetailsService's loadUserByUsername executed");
        Member foundMember = memberRepository.findByEmail(email);

        if (foundMember == null) {
            log.info(email + " not found");
            throw new UsernameNotFoundException(email + "not exists");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        MemberContext memberContext = new MemberContext(foundMember, roles);

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

    public Member signup(MemberDto memberDto) {
        log.info("CustomUserDetailsService executed");

        ModelMapper modelMapper = new ModelMapper();
        Member newMember = modelMapper.map(memberDto, Member.class);
        newMember.setPassword(passwordEncoder.encode(newMember.getPassword()));
        Member registeredUser = memberRepository.save(newMember);
        Authority authority = new Authority(
                registeredUser.getId().intValue(),
                registeredUser.getUsername(),
                "ROLE_USER");
        authoritiesRepository.save(authority);
        log.info(newMember.getUsername() + " has been registered");
        return registeredUser;
    }

    public Member findUserByUsername(String username) {
        Member foundUser = memberRepository.findByUsername(username);
        if (foundUser == null) {
            log.info(username + " user not found");
            return null;
        }
        return foundUser;
    }

    private Collection<GrantedAuthority> getAuthorities(String username) {
        List<Authority> authList = authoritiesRepository.findByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Authority authority : authList) {
            authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return authorities;
    }
}
