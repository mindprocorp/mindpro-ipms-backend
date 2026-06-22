package kr.co.mindpro.ipms.domain.ai.util;

import kr.co.mindpro.ipms.security.vo.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * AI 도구 실행 시 필요한 보안 컨텍스트 설정을 담당하는 헬퍼 클래스
 */
@Component
public class AiToolSecurityHelper {

    /**
     * AI 호출 컨텍스트에 사용자 정보를 설정합니다. (스레드 로컬)
     */
    public void setupAiSecurityContext(String officeSeq, String loginUser) {
        if (StringUtils.hasText(officeSeq)) {
            CustomUserDetails aiUser = new CustomUserDetails(
                "AI-Agent", 
                loginUser != null ? loginUser : "anonymous", 
                loginUser != null ? loginUser : "anonymous", 
                officeSeq, 
                Collections.emptyList()
            );
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(aiUser, null, aiUser.getAuthorities())
            );
        }
    }
}
