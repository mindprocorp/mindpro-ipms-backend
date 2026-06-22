package kr.co.mindpro.ipms.domain.ai.tool;

import kr.co.mindpro.ipms.common.util.SecurityUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * 접속한 사용자의 현재 권한 및 보안 그룹을 조회하는 도구
 */
@Component
public class SecurityPermissionSearchTool {

    @Tool(description = """
        현재 로그인한 사용자가 보유한 시스템 권한 목록을 조회합니다. 
        특정 메뉴 접근 가능 여부나 데이터 조회 범위가 제한적인지 확인할 때 사용하십시오.
        결과 규격: '보유권한: ROLE_USER,ROLE_ADMIN,CF_PARTNER' 등 콤마로 구분된 텍스트.
        """)
    public String getLoginUserPermissions() {
        String authorities = SecurityUtil.getAuthority();
        return authorities != null ? "보유권한: " + authorities : "보유권한: ANONYMOUS";
    }
}
