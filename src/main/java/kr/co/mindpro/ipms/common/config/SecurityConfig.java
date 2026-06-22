package kr.co.mindpro.ipms.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.servlet.DispatcherType;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import kr.co.mindpro.ipms.security.SecurityWhitelist;
import kr.co.mindpro.ipms.security.filter.JwtAuthenticationFilter;
import kr.co.mindpro.ipms.security.handler.JwtAuthenticationEntryPoint;
import kr.co.mindpro.ipms.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
	     	// 1. CORS 설정 적용
	        .cors(cors -> cors.configurationSource(corsConfigurationSource()))

	        // 2. CSRF 비활성화 (JWT를 사용하므로 세션 공격인 CSRF로부터 비교적 안전)
	        .csrf(csrf -> csrf.disable())

	        // 3. 폼 로그인 및 HTTP Basic 인증 비활성화 (REST API 전용 설정)
	        .formLogin(form -> form.disable())
	        .httpBasic(basic -> basic.disable())

	        // 4. 세션을 사용하지 않도록 설정 (Stateless)
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

	        // 5. 에러 핸들링 및 권한 설정
	        .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
	        .authorizeHttpRequests(auth -> auth
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ASYNC).permitAll() // [추가] Flux 비동기 스트리밍 & 에러 포워딩 시 SecurityContext 유실로 인한 401 방지
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // [추가] CORS Preflight(OPTIONS) 요청 조건 없이 전부 허용
	            .requestMatchers(SecurityWhitelist.PERMIT_ALL_ANT_PATTERNS).permitAll()

	            .anyRequest().authenticated()
	        )

	        // 6. JWT 필터 배치
	        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

	    return http.build();
    }

    /**
     * [CORS 설정 정의]
     * 프론트엔드 도메인에서의 접속을 허용합니다.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 사내 망(로컬 및 내부 IP) 및 허용된 외부 도메인만 CORS 허용
        configuration.addAllowedOriginPattern("http://localhost:*");
        configuration.addAllowedOriginPattern("http://192.168.*:*");
        //configuration.addAllowedOriginPattern("http://10.*:*");
        configuration.addAllowedOriginPattern("https://*.mindpro.co.kr"); // 예: 외부 실서버 도메인
        configuration.addAllowedOriginPattern("http://*.mindpro.co.kr"); // 예: 외부 실서버 도메인
        configuration.addAllowedOriginPattern("http://223.130.154.148*");
        configuration.addAllowedOriginPattern("http://223.130.154.148:*");
        configuration.addAllowedOriginPattern("http://192.168.2.*");
        configuration.addAllowedMethod("*");        // GET, POST, PUT, DELETE 모두 허용
        configuration.addAllowedHeader("*");        // 모든 헤더 허용
        configuration.setAllowCredentials(true);    // 내 자격증명(쿠키 등) 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 비밀번호 암호화 객체
     * BCrypt는 비밀번호 해싱을 위한 강력한 알고리즘으로, 2025년 기준 실무 표준입니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
