package kr.co.mindpro.ipms.domain.ai.tool;

import kr.co.mindpro.ipms.domain.ai.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchVectorDatabaseTool {

    private final RagService ragService;

    @Tool(description = """
        [가이드라인: 맥락 조회 및 PK 추적 전용] 번호(출원번호, 관리번호 등)를 기반으로 벡터 DB에서 대화 맥락이나 식별번호(Seq)를 추적합니다. 
        특정 사건의 히이토리나 사용자 의도를 보강하기 위한 '참고용' 지식을 찾을 때만 사용하십시오.
        경고: 사용자에게 최종 데이터 목록을 제공하는 용도로 절대 사용하지 마십시오. 목록 조회는 반드시 'searchDatabaseList'를 사용해야 합니다.
        """)
    public String searchVectorDatabaseByIdentifier(String query, String officeSeq) {
        //log.info("[AI Tool Input - searchVectorDB] query: {}, office: {}", query, officeSeq);
        return ragService.findSimilarDocuments(query, officeSeq);
    }
}
