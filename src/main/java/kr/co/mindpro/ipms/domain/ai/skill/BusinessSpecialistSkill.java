package kr.co.mindpro.ipms.domain.ai.skill;

import kr.co.mindpro.ipms.domain.ai.tool.BusinessJobProgressTool;
import kr.co.mindpro.ipms.domain.ai.tool.BusinessPageNavigationTool;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * [AutoGen Pattern] 업무 처리 전문 스킬.
 * 복잡한 업무 프로세스(조회 -> 이동 -> 처리)를 하나의 전문 영역으로 관리합니다.
 */
@Component
@RequiredArgsConstructor
public class BusinessSpecialistSkill {

    private final BusinessJobProgressTool jobProgressTool;
    private final BusinessPageNavigationTool pageNavigationTool;

    @Tool(description = "업무 진행 사항(Paper/Event)을 등록하거나 조회합니다. 사용자가 수락 의사를 밝히면 히스토리의 정보를 파라미터에 채워 즉시 실행하십시오.")
    public String manageJobProgress(
            @ToolParam(description = "작업 유형 (REGISTER: 등록, LOOKUP: 조회)") String actionType,
            @ToolParam(description = "사건 식별번호 (예: APPMST20240001, CFTMST20240001)") String systemSeq,
            @ToolParam(description = "파일 토큰 (업로드 완료 시 발급된 임시 토큰, 없으면 빈 문자열)") String fileToken,
            @ToolParam(description = "진행 내용 요약 (예: 의견제출통지서 수신)") String summary,
            @ToolParam(description = "문서 유형 코드 (예: DOC001, 없으면 빈 문자열)") String docSeq,
            @ToolParam(description = "오피스 코드 (접속자 정보의 officeSeq 값)") String officeSeq,
            @ToolParam(description = "처리 사용자 식별번호 (접속자 정보의 loginUser 값)") String userSeq
    ) {
        Map<String, Object> progressData = new HashMap<>();
        progressData.put("systemSeq", systemSeq != null ? systemSeq : "");
        progressData.put("fileToken", fileToken != null ? fileToken : "");
        progressData.put("summary", summary != null ? summary : "");
        progressData.put("docSeq", docSeq != null ? docSeq : "");
        progressData.put("officeSeq", officeSeq != null ? officeSeq : "");
        progressData.put("userSeq", userSeq != null ? userSeq : "");
        return jobProgressTool.businessJobProgress(actionType, progressData);
    }

    @Tool(description = "시스템의 특정 화면으로 즉시 이동합니다. 'urlset'에서 얻은 전체 경로(fullUrl)를 우선 사용하세요.")
    public String navigateToPage(
            @ToolParam(description = "스크린 코드 (예: DOMSET030 등)") String screenCode,
            @ToolParam(description = "대상 ID (필요 시)") String targetId,
            @ToolParam(description = "전체 이동 경로 (추천)") String fullUrl
    ) {
        return pageNavigationTool.navigatePage(screenCode, targetId, fullUrl);
    }
}
