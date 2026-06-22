package kr.co.mindpro.ipms.security.provider;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.security.vo.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;
    
    @Value("${jwt.refresh-expiration:604800000}") // Refresh Token 만료 시간 (기본 7일)
    private long refreshExpirationTime;    

    private SecretKey key;
    
    private static final long PASSWORD_RESET_EXPIRATION = 5 * 60 * 1000L; // 5분

    // 권한 정보를 담을 클레임 키 이름
    private static final String AUTHORITIES_KEY = "auth";
    private static final String USER_INFO_SEQ_KEY = "userInfoSeq";
    private static final String USER_MST_SEQ_KEY = "userMstSeq";
    private static final String OFFICE_SEQ_KEY = "officeSeq";
    private static final String PURPOSE_KEY = "purpose";
    private static final String PURPOSE_PASSWORD_RESET = "PASSWORD_RESET";

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Access Token 생성
     */
    public String createToken(String username, String userInfoSeq, String userMstSeq, String officeSeq, String role) {
        return buildToken(username, userInfoSeq, userMstSeq, officeSeq, role, expirationTime);
    }    
    
    /**
     * 추가: Refresh Token 생성
     */
    public String createRefreshToken(String username, String userInfoSeq, String userMstSeq, String officeSeq, String role) {
        return buildToken(username, userInfoSeq, userMstSeq, officeSeq, role, refreshExpirationTime);
    }
    
    /**
     * 공통 토큰 빌더
     */
    private String buildToken(String username, String userInfoSeq, String userMstSeq, String officeSeq, String role, long validityTime) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username)
                .claim(USER_INFO_SEQ_KEY, userInfoSeq)
                .claim(USER_MST_SEQ_KEY, userMstSeq)
                .claim(OFFICE_SEQ_KEY, officeSeq)
                .claim(AUTHORITIES_KEY, role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + validityTime))
                .signWith(key)
                .compact();
    }
    
    /**
     * 토큰에서 사용자 아이디(Subject) 추출
     */
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }   
    
    /**
     * 토큰에서 권한 정보 추출
     */
    public String getRole(String token) {
        return parseClaims(token).get(AUTHORITIES_KEY, String.class);
    }
    
    /**
     * 토큰에서 userInfoSeq 추출
     */
    public String getUserInfoSeq(String token) {
        return parseClaims(token).get(USER_INFO_SEQ_KEY, String.class);
    }    
    
    /**
     * 토큰에서 userMstSeq 추출
     */
    public String getUserMstSeq(String token) {
        return parseClaims(token).get(USER_MST_SEQ_KEY, String.class);
    }    
    
    /**
     * 토큰에서 officeSeq 추출
     */
    public String getOfficeSeq(String token) {
        return parseClaims(token).get(OFFICE_SEQ_KEY, String.class);
    }
    
    /**
     * 토큰 복호화 및 클레임 추출
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }    

    /**
     * 토큰 내부의 Claim에서 권한 정보를 꺼내어 Authentication 객체를 생성합니다.
     */    
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        
        String userInfoSeq = claims.get(USER_INFO_SEQ_KEY, String.class);
        String userMstSeq = claims.get(USER_MST_SEQ_KEY, String.class);
        String officeSeq = claims.get(OFFICE_SEQ_KEY, String.class);
        
        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);	// Claim에서 권한 문자열 추출
        if (authoritiesClaim == null) {
            log.error("권한 정보가 없는 토큰입니다. Token: {}", token);
            throw new BusinessException(ErrorCode.INVALID_TOKEN);             
        }
        
        // 문자열을 GrantedAuthority 리스트로 변환
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(authoritiesClaim.toString().split(","))
            .map(role -> {
                String roleName = role.toUpperCase().trim();
                // DB 값이 "ROLE_USER"라면 그대로 사용, "USER"라면 "ROLE_"를 붙임
                return new SimpleGrantedAuthority(roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName);
            })        		
        		.collect(Collectors.toList());
        
        CustomUserDetails principal = new CustomUserDetails(claims.getSubject(), userInfoSeq, userMstSeq, officeSeq, authorities);
        
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);        
    }
    
    public String createResponseCookie(String name, String token, long maxAge) {
        return ResponseCookie.from(name, token)
                .httpOnly(true)
                .secure(true) // HTTPS 필수
                .path("/")
                .maxAge(maxAge / 1000)
                .sameSite("Lax") // CSRF 방어와 유연성 사이의 균형
                .build()
                .toString();
    }

    /**
     * 비밀번호 재설정 전용 토큰 생성 (15분 만료)
     */
    public String createPasswordResetToken(String userId) {
        Date now = new Date();
        return Jwts.builder()
                .subject(userId)
                .claim(PURPOSE_KEY, PURPOSE_PASSWORD_RESET)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + PASSWORD_RESET_EXPIRATION))
                .signWith(key)
                .compact();
    }

    /**
     * 비밀번호 재설정 토큰 검증 후 userId 반환
     */
    public String validatePasswordResetToken(String token) {
        Claims claims = parseClaims(token); // 만료·서명 오류 시 BusinessException 발생
        String purpose = claims.get(PURPOSE_KEY, String.class);
        if (!PURPOSE_PASSWORD_RESET.equals(purpose)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | io.jsonwebtoken.MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
    }
}
