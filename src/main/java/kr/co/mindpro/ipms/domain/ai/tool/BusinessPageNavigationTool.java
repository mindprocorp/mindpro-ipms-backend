package kr.co.mindpro.ipms.domain.ai.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.Map;

@Component
public class BusinessPageNavigationTool {

    @Tool(description = """
        사용자를 시스템 내 특정 페이지로 이동시킵니다. 
        사용법: 'searchFrontAllRoutes'에서 얻은 경로 패턴에 실제 ID(appSeq 등)를 결합한 'fullUrl'을 우선 사용하십시오.
        결과 규격: 'SUCCESS|COMMAND_NAV|최종URL' 형식의 명령어를 반환하여 프론트엔드 이동을 수행합니다.
        """)
    public String navigatePage(String screenCode, String targetId, String fullUrl) {
        
        // 1. 직접 조립된 fullUrl이 있는 경우 우선 사용 (urlset 방식)
        if (StringUtils.hasText(fullUrl)) {
            return "SUCCESS|COMMAND_NAV|" + fullUrl;
        }

        // 2. 레거시 방식 (스크린코드 기반)
        if (StringUtils.hasText(screenCode)) {
            return "SUCCESS|COMMAND_NAV|%s|%s".formatted(screenCode, (targetId != null ? targetId : ""));
        }

        return "ERROR|이동할 대상 정보가 부족합니다.";
    }
}
