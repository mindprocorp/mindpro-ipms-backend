package kr.co.mindpro.ipms.domain.conflict.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.util.CommonMapping;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import kr.co.mindpro.ipms.domain.conflict.dto.enums.ConflictCourtCategoryCode;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 의뢰심판 테이블 매핑 객체
 * DB 테이블 ipms_user 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : OppoVO.java
 * @since	 : 2026. 01. 07.
 */
    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    @Schema(description = "이의심판 마스터 정보 (순수 심판 고유 필드)")
    public class ConflictMstVO extends BaseVO {

        @Schema(description = "이의심판 시퀀스 (PK)", example = "CFT20260116-001")
        private String conflictSeq;

        @Schema(description = "OurRef (출원연결키)", example = "1")
        private String ourRef;

        @Schema(description = "YourRef", example = "Y-12345")
        private String yourRef;

        @Schema(description = "사무소 시퀀스 (PK)", example = "OFFICE_001")
        private String officeSeq;

        @Schema(description = "출원 연동 시퀀스 (FK)", example = "APP_2025_999")
        private String appSeq;

        @Schema(description = "심판 사건번호", example = "2026-당-1234")
        private String litigationCaseNo;

        @Schema(description = "대리인구분", example = "TRIAL_A")
        private String agentCategoryCode;

    @Schema(description = "대리인구분코드이름", example = "TRIAL_A")
    private String agentCategoryCodeName;

        @Schema(description = "계류법정", example = "TRIAL_A")
        private String courtCategoryCode;
    @Schema(description = "계류법정", example = "TRIAL_A")
    private String courtCategoryCodeName;

    @Schema(description = "구분", example = "TRIAL_A")
    //private String caseCategoryCode;
    private String appClassificationCode;
    @Schema(description = "구분", example = "TRIAL_A")
    //private String caseCategoryCodeName;
    private String appClassificationCodeName;

        @Schema(description = "사건구분코드", example = "CAT_01")
        private String caseTypeCode;
    @Schema(description = "사건구분코드", example = "CAT_01")
    private String caseTypeCodeName;

        @Schema(description = "진행상태", example = "ING")
        private String status;
        @Schema(description = "진행상태", example = "ING")
        private String statusCodeName;

        @Schema(description = "청구인 유형", example = "원고")
        private String petitionerType;

        @Schema(description = "청구인 메모", example = "담당자 메모: 증거 보완 필요")
        private String petitionerMemo;

        @Schema(description = "피청구인 유형", example = "피고")
        private String respondentType;

        @Schema(description = "피청구인 메모", example = "피청구인 답변서 제출 기한 연장")
        private String respondentMemo;

        @Schema(description = "심사전치결과", example = "유지")
        private String preExamResult;

        @Schema(description = "최종결과", example = "승소")
        private String finalResult;

        @Schema(description = "결정내용 상세", example = "청구인의 모든 심판 청구를 기각 결정함")
        private String decisionContent;

        @Schema(description = "불복제기 내용")
        private String appealContent;

        @Schema(description = "포기여부")
        private String abandonYn;

        @Schema(description = "포기여부")
        private String abandonContent;

        @Schema(description = "부서이름")
        private String deptName;

        @Schema(description = "비고")
        private String note;


        @Schema(description = "기타사건여부")
        private String etcYn;

        @Schema(description = "상대방")
        private String respondent;
        @Schema(description = "소개자")
        private String introducer;
        @Schema(description = "사건명")
        private String caseTitleKo;

        @Schema(description = "해외대리인 (이름)")
        private String foreignAgent;

        @Schema(description = "의뢰인 (이름)")
        private String client;

}

