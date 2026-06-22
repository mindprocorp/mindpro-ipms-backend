package kr.co.mindpro.ipms.domain.ai.tool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import kr.co.mindpro.ipms.domain.jobprogress.service.JobProgressService;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessJobProgressTool {

    private final JobProgressService jobProgressService;
    private final CacheManager cacheManager;

    @Tool(description = """
        사건의 새로운 '업무 진행 사항(Paper/Event)'을 등록하거나 상세 정보를 조회합니다. 
        문서 수신, 마감일 설정 등 실제 행정 업무를 시스템에 기록할 때 사용하십시오.
        결과 규격: '작업결과: SUCCESS|FAIL', '식별번호: 값' 형태의 텍스트로 반환됩니다.
        """)
    public String businessJobProgress(String actionType, Map<String, Object> progressData) {
        try {
            String systemSeq = (String) progressData.get("systemSeq");
            String fileToken = (String) progressData.get("fileToken");
            String summary = (String) progressData.get("summary");
            String docSeq = (String) progressData.get("docSeq");
            String officeSeq = (String) progressData.get("officeSeq");
            String userSeq = (String) progressData.get("userSeq");

            String realFileSeq = cacheManager.getCache("tempFileTokens").get(userSeq + ":" + fileToken, String.class);
            if (realFileSeq == null) return "파일 등록 가능 시간이 만료되었거나 토큰이 유효하지 않습니다.";
            return jobProgressService.registerProgressForAi(systemSeq, realFileSeq, summary, docSeq, officeSeq, userSeq);
        } catch (Exception e) {
            log.error("업무 등록 실패", e);
            return "등록 중 에러 발생: " + e.getMessage();
        }
    }
}
