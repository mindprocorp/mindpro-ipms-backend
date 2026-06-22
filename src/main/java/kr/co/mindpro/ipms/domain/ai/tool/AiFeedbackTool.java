package kr.co.mindpro.ipms.domain.ai.tool;

import kr.co.mindpro.ipms.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * AI 학습 및 성능 측정을 위한 사용자 피드백 수집 도구
 */
@Slf4j
@Component
public class AiFeedbackTool {

    /**
     * AI의 답변이나 검색 결과에 대해 사용자의 피드백을 기록합니다.
     * 이 데이터는 추후 강화학습(RL) 및 벡터 가중치 조정에 활용됩니다.
     * 
     * @param originalQuery      사용자가 입력한 원래 질문
     * @param selectedRecordId   AI가 제시한 데이터 중 사용자가 선택하거나 확정한 PK (없으면 "NONE")
     * @param feedbackType       피드백 종류 (SUCCESS: 만족, FAIL: 불만족, CORRECTION: 수정 요청)
     * @param correctionContent  사용자가 직접 말한 수정 내용 (예: "이거 말고 XX 건이야")
     */
    @Tool(description = "AI 학습을 위해 사용자의 검색 결과 만족도나 수정 요청 사항을 기록합니다. 사용자가 답변을 정정하거나 긍정/부정 표현을 할 때 반드시 호출하십시오.")
    public String logAiFeedback(
            @ToolParam(description = "사용자의 원본 질문") String originalQuery,
            @ToolParam(description = "확정된 데이터 식별자 (모르면 'NONE')") String selectedRecordId,
            @ToolParam(description = "피드백 유형 (SUCCESS, FAIL, CORRECTION)") String feedbackType,
            @ToolParam(description = "사용자가 언급한 정정 내용") String correctionContent
    ) {
        String userSeq = SecurityUtil.getUserInfoSeq();
        String userMstSeq = SecurityUtil.getUserMstSeq();
        String officeSeq = SecurityUtil.getOfficeSeq();

        log.info(">>>> [AI LEARNING LOG] User: {}, MstSeq: {}, Office: {}, Type: {}, Query: {}, Record: {}, Correction: {}", 
                userSeq, userMstSeq, officeSeq, feedbackType, originalQuery, selectedRecordId, correctionContent);

        return String.format("피드백이 기록되었습니다. (유형: %s). 사용자님의 선호도가 다음 검색에 반영됩니다.", feedbackType);
    }

    /**
     * AI의 내부 추론 과정 및 자아 성찰 기록 (Self-Correction 용도)
     */
    @Tool(description = "도구 실행 결과가 예상을 벗어나거나 빈 결과가 나왔을 때, 자신의 추론 과정을 스스로 성찰하고 분석하여 기록합니다. 재시도 전 반드시 호출하십시오.")
    public String logInternalReflection(
            @ToolParam(description = "직전에 실행한 도구와 파라미터") String lastAction,
            @ToolParam(description = "빈 결과나 에러가 나온 원인 분석") String analysis,
            @ToolParam(description = "다음 단계로 계획한 수정 전략") String correctionPlan
    ) {
        log.warn(">>>> [AI SELF-REFLECTION] Action: {}, Analysis: {}, Plan: {}", lastAction, analysis, correctionPlan);
        return "성찰 내용이 시스템에 기록되었습니다. 제안하신 수정 전략[" + correctionPlan + "]을 기반으로 재시도를 진행하십시오.";
    }
}
