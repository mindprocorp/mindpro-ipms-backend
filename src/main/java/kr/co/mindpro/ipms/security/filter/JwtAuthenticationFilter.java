package kr.co.mindpro.ipms.security.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.mindpro.ipms.security.SecurityWhitelist;
import kr.co.mindpro.ipms.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * JWT 검증을 아예 건너뛸 경로. {@link SecurityWhitelist#PERMIT_ALL_PREFIXES} 참조 —
     * SecurityConfig 의 permitAll 과 항상 동기화 유지.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (String allow : SecurityWhitelist.PERMIT_ALL_PREFIXES) {
            if (path.startsWith(allow)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (StringUtils.hasText(token)) {
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    Authentication auth = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.debug("### [JWT Filter] Authentication set: {}, Roles: {}", auth.getName(), auth.getAuthorities());
                }
            } catch (Exception ex) {
                // 만료/위변조 등 토큰 예외는 throw 대신 SecurityContext 비우고 통과 →
                // 보호된 엔드포인트는 SecurityConfig의 EntryPoint가 401 응답.
                // 이 401을 받은 프론트가 /api/auth/refresh 자동 재시도(reactive refresh)를 수행한다.
                SecurityContextHolder.clearContext();
                log.debug("### [JWT Filter] Token invalid (will return 401 if endpoint protected): {}", ex.getMessage());
            }
        } else {
            log.debug("### [JWT Filter] Token is missing. Path: {}", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    log.debug("### [JWT Filter] Token found in Cookie");
                    return cookie.getValue();
                }
            }
        }
        
        return null;
    }
}
