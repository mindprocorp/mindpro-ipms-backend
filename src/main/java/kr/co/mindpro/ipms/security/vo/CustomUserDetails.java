package kr.co.mindpro.ipms.security.vo;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Spring Security 인증 객체 (토큰 내 사용자 정보를 담는 컨텍스트)
 * @AuthenticationPrincipal CustomUserDetails userDetails 추가 하여 사용
 * 
 * @author   : intst
 * @since    : 2026. 1. 12.
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final String username;      	// 사용자 ID (Subject)
    private final String userInfoSeq;	// 사용자 정보 SEQ
    private final String userMstSeq;    	// 사용자 마스터 SEQ
    private final String officeSeq;		// 소속 사무실 SEQ
    private final Collection<? extends GrantedAuthority> authorities; // 권한 목록

    public CustomUserDetails(String username, String userInfoSeq, String userMstSeq, String officeSeq, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.userInfoSeq = userInfoSeq;
        this.userMstSeq = userMstSeq;
        this.officeSeq = officeSeq;
        this.authorities = authorities;
    }    
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null; // JWT 인증이므로 패스워드는 관리하지 않음
    }

    @Override
    public String getUsername() {
        return username;
    }

    /* 계정 상태 관리 (요구사항에 따라 로직 추가 가능) */
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
