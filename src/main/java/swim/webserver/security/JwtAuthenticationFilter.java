package swim.webserver.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter implements Filter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Optional<String> token = resolveToken((HttpServletRequest) request);

        // 토큰 유효성 검사
        if (token.isPresent() && jwtTokenProvider.validateToken(token.get())) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token.get());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private Optional<String> resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }
}
