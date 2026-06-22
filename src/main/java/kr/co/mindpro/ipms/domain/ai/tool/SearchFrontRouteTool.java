package kr.co.mindpro.ipms.domain.ai.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SearchFrontRouteTool {

    @Value("${app.front-url:http://localhost:4000}")
    private String frontUrl;

    @Tool(description = """
        현재 설정된 프론트엔드 URL을 반환합니다.
        상세 페이지 링크 생성 시 이 URL을 prefix로 사용하십시오.
        예: front-url + /domestic/detail/123 → http://localhost:4000/domestic/detail/123
        """)
    public String getFrontUrl() {
        return "프론트엔드 URL: " + frontUrl;
    }
}
