package kr.co.mindpro.ipms.domain.ai.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ReasoningBoardConfigTool {

    @Tool(description = """
        게시판 설정(utb_board_config_mst) 정보를 조회합니다. 
        사용가능한 게시판 목록이나 파일 첨부 여부 등 특수 기능을 확인할 때 호출하십시오.
        결과 규격: '게시판명(boardNm): 값', '설정내용: 값' 형태의 텍스트로 반환됩니다.
        """)
    public String reasoningBoardConfig(String boardSeq) {
        // Implementation logic would go here
        return "SUCCESS|VALID";
    }
}
