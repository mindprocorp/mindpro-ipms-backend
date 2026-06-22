package kr.co.mindpro.ipms.domain.ai.util;

import kr.co.mindpro.ipms.domain.ai.util.AiSkillLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AiPromptManager {

    private final AiSkillLoader skillLoader;

    public String getSystemPrompt(String userInfoSeq, String currentDate, String officeSeq,
                                  String history, String context, String message, String userMstSeq) {

        String skillSpecifications = skillLoader.loadAllSkills();

        String template = """
        당신은 IPMS(지식재산관리시스템) 전문 AI 비서입니다. 특허·실용신안·디자인·상표 등 산업재산권 관련 전문 지식을 보유하고 있으며,
        사내 지식과 아래의 실행 도구 명세(Skills)를 완벽히 숙지하여 답변하세요.

        [🚨 절대 출력 금지 - 위반 시 시스템 오류]
        아래 항목들은 사용자 화면에 절대 출력하지 마십시오:
        - "계획", "실행", "검증", "자가치유", "재시도", "수정된 계획" 같은 단계 제목
        - [Thinking], [사고], [내부 추론], [COT], [Planning], [Self-Healing] 등 모든 추론 섹션 헤더
        - "1차 시도:", "2차 시도:", "추가 확인:", "현재 상황:", "수정된 계획:" 같은 내부 진행 문구
        - searchDatabaseList, fetchRealtimeDatabaseRecord 등 영문 메서드명
        - dueLimitDate, claimDate, appSeq, etcYn 등 영문 필드명
        오직 최종 결과 마크다운만 출력하십시오. 모든 추론은 내부적으로만 처리하십시오.

        [내부 처리 규칙 - 출력 금지]
        - 질문 해결에 필요한 변수(PK, 필드명)를 먼저 결정하고 벡터 DB에서 필드 매핑 정보를 찾습니다.
        - 특허번호/출원번호 형식(10-, 20-, 30-, 40-)으로 유형을 판단합니다.
        - 최적의 도구를 실행하고, 사내 데이터가 없으면 즉시 KIPRIS 외부 도구를 연계합니다.
        - 결과가 "없음"이면 즉시 답변하지 말고: 쿼리 파라미터(날짜 오타, 과도한 필터 등)를 수정하여 최소 1회 재조회합니다.
        - 필터를 줄이거나 도메인을 재검토한 뒤 재시도하고, 끝내 없으면 KIPRIS 외부 도구로 넘어갑니다.
        - 반환된 날짜·상태값이 현재일(%s 기준)과 비교하여 논리적으로 맞는지 확인합니다.

        [🚨 도구 사용 계층 (Tool Hierarchy - Facts vs. Context)]
        - **Facts (SQL)**: 날짜, 상태, 상세 목록 조회 시 반드시 `searchDatabaseList`를 물리적으로 호출하십시오.
        - **Context (RAG)**: 벡터 DB는 계획 단계에서 변수를 찾거나 대화 맥락을 보강하는 용도로만 사용하십시오.
        - **KIPRIS**: 사내 DB 조회 실패 시 또는 실시간 행정 이력이 필요한 경우 우선 연동하십시오.

        [🔐 보안 원칙 (Security First)]
        - 모든 데이터 접근 및 시스템 변경 전 반드시 `SecurityPermissionSearchTool`로 권한을 먼저 확인하십시오.
        - 권한이 없는 작업에는 명확한 거절 메시지와 담당자 문의 안내를 제공하십시오.

        [🎨 UI 에스테틱 2.5 (High-Density Table & Clean UI)]
        - 목록(2건 이상) -> **마크다운 표(Table)** (영문/괄호 제거 필수).
        - 단일건/요약 -> **수직 카드** (`---` 구분).
        - 기술 용어(appSeq, officeSeq 등)는 사용자에게 절대 노출하지 마십시오.

        [접속자 정보] - 오피스(officeSeq): %s | 현재일시: %s | 사용자: %s | 마스터Seq: %s

        [🛠️ 실행 도구 상세 명세 (Skills Specification)]
        {SKILL_SPECIFICATIONS}

        [대화 내역 (History)]
        %s

        [사내 지식 (RAG Context)]
        %s

        [사용자 질문]
        %s
        """;

        return template
                .formatted(currentDate, officeSeq, currentDate, userInfoSeq, userMstSeq, history, context, message)
                .replace("{SKILL_SPECIFICATIONS}", skillSpecifications);
    }

    /**
     * 사용자 메시지의 의도(Intent)를 분석하여 적합한 도구 그룹을 결정합니다.
     *
     * @return SEARCH / BUSINESS / KIPRIS / GENERAL 중 하나
     */
    public String getIntent(String message) {
        if (message == null || message.isBlank()) return "GENERAL";

        String lower = message.toLowerCase();

        // KIPRIS 외부 조회 의도 감지
        if (lower.contains("kipris") || lower.contains("키프리스") || lower.contains("외부조회")
                || lower.contains("행정이력") || lower.contains("법적상태") || lower.contains("특허청")
                || lower.matches(".*\\b(10|20|30|40)-\\d{4}-\\d+.*")) {
            return "KIPRIS";
        }

        // 업무 등록 / 화면 이동 의도 감지
        if (lower.contains("등록해") || lower.contains("등록하") || lower.contains("업무등록")
                || lower.contains("진행등록") || lower.contains("화면이동") || lower.contains("화면으로")
                || lower.contains("이동해") || lower.contains("이동하") || lower.contains("바로가기")
                || lower.contains("수신등록") || lower.contains("처리해") || lower.contains("처리하")
                || lower.contains("응") && lower.length() < 15  // 짧은 긍정 답변(암시적 동의)
                || lower.contains("어") && lower.length() < 10
                || lower.contains("그래") && lower.length() < 15
                || lower.contains("ㅇㅇ") && lower.length() < 10) {
            return "BUSINESS";
        }

        // 날짜 계산 / 논리 / 보안 의도 감지
        if (lower.contains("날짜") || lower.contains("기간") || lower.contains("마감") && lower.contains("계산")
                || lower.contains("개월") || lower.contains("이후") && lower.contains("날")
                || lower.contains("권한") || lower.contains("접근") && lower.contains("가능")
                || lower.contains("설정") || lower.contains("보드") || lower.contains("환경설정")) {
            return "GENERAL";
        }

        // 데이터 검색 의도 감지 (기본 폴백)
        if (lower.contains("조회") || lower.contains("검색") || lower.contains("찾아")
                || lower.contains("알려") || lower.contains("목록") || lower.contains("리스트")
                || lower.contains("출원") || lower.contains("심판") || lower.contains("이의")
                || lower.contains("상표") || lower.contains("디자인") || lower.contains("특허")
                || lower.contains("고객") || lower.contains("청구") || lower.contains("비용")
                || lower.contains("해외") || lower.contains("분쟁")) {
            return "SEARCH";
        }

        // 명확하지 않은 경우 전체 도구 로드 (SEARCH로 폴백)
        return "SEARCH";
    }
}
