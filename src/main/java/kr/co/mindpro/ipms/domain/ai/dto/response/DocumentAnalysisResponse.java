package kr.co.mindpro.ipms.domain.ai.dto.response;

import lombok.Builder;

@Builder
public record DocumentAnalysisResponse(
        String documentType,        // AI가 분석한 문서 종류 (예: 의견제출통지서)
        String extractedIdentifier, // 문서에서 추출한 원본 번호 (예: 10-2026-1234)
        String identifierType,      // 번호의 성격 (OUR_REF, APP_NO, CASE_NO 등)
        String summary,             // 한글 요약
        String systemSeq,           // 벡터 DB로 찾아낸 실제 시스템 PK (APPMST...)
        String officeSeq,           // 소속 사무소 코드
        String docSeq,              // 문서마스터 번호 (databaseTools로 찾은 값)
        String eventDate,           // [추가] 문서 발생일 (YYYYMMDD) - 등록 시 필수!
        String direction,           // [추가] INBOUND/OUTBOUND 구분
        String fileToken, // [추가] 실제 FILE_SEQ 대신 보낼 가짜 ID
        String message,           // [추가] AI가 작성한 화면 출력용 브리핑 문구
        boolean isRegisteredToNextStep // 자동 등록 성공 여부 (이제 분석단계에선 항상 false)
) {}
