package kr.co.mindpro.ipms.security;

/**
 * 인증/JWT 검증 없이 통과해야 하는 공개 경로 목록.
 *
 * SecurityConfig 의 {@code permitAll(...)} 과 JwtAuthenticationFilter 의
 * {@code shouldNotFilter(...)} 가 같은 source 를 참조하도록 한 곳에 모아둔다.
 *
 * - SecurityConfig 는 Ant 패턴이 필요하므로 {@link #PERMIT_ALL_ANT_PATTERNS}
 * - JwtAuthenticationFilter 는 prefix startsWith 매칭이므로 {@link #PERMIT_ALL_PREFIXES}
 *
 * 추가/변경 시 두 배열은 함께 유지한다 (의미상 1:1 매칭).
 */
public final class SecurityWhitelist {

    private SecurityWhitelist() {}

    /** SecurityConfig.requestMatchers(...).permitAll() 용 Ant 패턴 */
    public static final String[] PERMIT_ALL_ANT_PATTERNS = {
            "/api/auth/**",
            "/api/users/register/**",
            "/api/v1/common/registry/public/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/demo-ui.html",
            "/error",
    };

    /** JwtAuthenticationFilter.shouldNotFilter() 용 startsWith prefix */
    public static final String[] PERMIT_ALL_PREFIXES = {
            "/api/auth/",
            "/api/users/register/",
            "/api/v1/common/registry/public/",
            "/swagger-ui/",
            "/v3/api-docs/",
            "/demo-ui.html",
            "/error",
    };
}
