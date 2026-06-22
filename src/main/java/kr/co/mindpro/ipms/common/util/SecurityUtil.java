package kr.co.mindpro.ipms.common.util;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import kr.co.mindpro.ipms.security.vo.CustomUserDetails;

public class SecurityUtil {
    
	private SecurityUtil() {
        // 인스턴스화 방지
    }

    /**
     * 현재 인증된 CustomUserDetails를 Optional로 반환
     */
    public static Optional<CustomUserDetails> getCurrentUserDetails() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof CustomUserDetails)
                .map(CustomUserDetails.class::cast);
    }

    /** 사용자 ID (Username) 조회 */
    public static String getUsername() {
        return getCurrentUserDetails().map(CustomUserDetails::getUsername).orElse(null);
    }

    /** 사용자 정보 SEQ 조회 */
    public static String getUserInfoSeq() {
        return getCurrentUserDetails().map(CustomUserDetails::getUserInfoSeq).orElse(null);
    }

    /** 사용자 마스터 SEQ 조회 */
    public static String getUserMstSeq() {
        return getCurrentUserDetails().map(CustomUserDetails::getUserMstSeq).orElse(null);
    }

    /** 소속 사무실 SEQ 조회 */
    public static String getOfficeSeq() {
        return getCurrentUserDetails().map(CustomUserDetails::getOfficeSeq).orElse(null);
    }

    /** 
     * 권한 정보를 콤마(,)로 구분된 문자열로 반환 (예: "ROLE_USER,ROLE_ADMIN")
     */
    public static String getAuthority() {
        return getCurrentUserDetails()
                .map(user -> user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .orElse(null);
    }

    /** 권한 목록(Collection) 조회 */
    public static Collection<? extends GrantedAuthority> getAuthorities() {
        return getCurrentUserDetails().map(CustomUserDetails::getAuthorities).orElse(null);
    }

    /** 
     * 현재 사용자가 특정 권한을 가지고 있는지 확인 
     */
    public static boolean hasRole(String role) {
        String roleName = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        Collection<? extends GrantedAuthority> authorities = getAuthorities();
        return authorities != null && authorities.stream()
                .anyMatch(a -> a.getAuthority().equals(roleName));
    }
}
