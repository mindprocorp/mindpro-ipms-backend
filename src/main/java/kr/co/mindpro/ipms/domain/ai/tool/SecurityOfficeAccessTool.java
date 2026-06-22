package kr.co.mindpro.ipms.domain.ai.tool;

import kr.co.mindpro.ipms.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityOfficeAccessTool {

    @Tool(description = """
        사용자가 특정 사무소(Office) 데이터에 접근할 권한이 있는지 확인합니다. 
        모든 데이터 조회 및 등록 전 반드시 해당 사용자의 소속과 권한을 교차 검증하십시오.
        결과 규격: '권한확인: SUCCESS|FAIL' 형태의 텍스트로 반환됩니다.
        """)
    public String securityOfficeAccess(String officeSeq, String userSeq) {
        String currentOffice = SecurityUtil.getOfficeSeq();
        
        if (currentOffice != null && currentOffice.equals(officeSeq)) {
            return "권한확인: SUCCESS";
        }
        
        log.warn("보안 위반 감지: 현재오피스({}), 요청오피스({})", currentOffice, officeSeq);
        return "권한확인: FAIL";
    }
}
