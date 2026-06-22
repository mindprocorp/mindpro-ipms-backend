package kr.co.mindpro.ipms.domain.ai.tool;

import kr.co.mindpro.ipms.common.util.SecurityUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class SecurityAdminCheckTool {

    @Tool(description = """
        사용자가 시스템 관리자(Admin) 권한을 보유했는지 확인합니다. 
        게시판 설정 변경, 코드 관리 등 민감한 시스템 설정 업무 수행 전 반드시 호출하십시오.
        결과 규격: '어드민확인: SUCCESS|FAIL' 형태의 텍스트로 반환됩니다.
        """)
    public String securityAdminCheck(String userSeq) {
        boolean isAdmin = SecurityUtil.hasRole("ADMIN");
        return isAdmin ? "어드민확인: SUCCESS" : "어드민확인: FAIL";
    }
}
