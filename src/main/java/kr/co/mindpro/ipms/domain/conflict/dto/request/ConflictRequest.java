package kr.co.mindpro.ipms.domain.conflict.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.util.CommonMapping;
import kr.co.mindpro.ipms.domain.conflict.vo.ConflictMergeVO;
import kr.co.mindpro.ipms.domain.conflict.vo.ConflictMstVO;
import lombok.Builder;

/**
 * 분쟁/심판 관련 기초 정보 통합 DTO (Record 버전)
 */
public class ConflictRequest {

    @Builder
    @Schema(description = "분쟁/심판 상세 요청 데이터")
    public record ConflictDetail(
            @Schema(description = "분쟁 일련번호 (수정 시 사용)") String conflictSeq,
            ConflictCftCaseMng cftCaseMng,
            ConflictAppBaseInfo appBaseInfo,
            ConflictAppTitleInfo appTitleInfo,
            ConflictAppGoodsInfo appGoodsInfo,
            ConflictAppPartyInfo appPartyInfo,
            ConflictCftLitigantInfo cftLitigantInfo,
            ConflictCftJudgmentInfo cftJudgmentInfo,
            ConflictCftManagerInfo cftManagerInfo,
            ConflictCftNoteInfo cftNoteInfo
            , ConflictResultList cftResultList
    ) {
    }

    @Builder
    @Schema(description = "분쟁 결과 단건 저장/수정 요청 (화면 키 기준)")
    public record ResultDetail(
            @Schema(description = "이의심판 시퀀스", example = "CFT20260116-001")
            String conflictSeq,
            String conflictResultSeq,
            @Schema(description = "사건번호", example = "2026당123456") String
            resultCaseNo,
            @Schema(description = "심결문 조회 URL")
            String judgmentSearchUrl,
            @Schema(description = "청구일", example = "20260223", format = "YYYYMMDD")
            String resultRequestDate,
            @Schema(description = "판결일(종결일)", example = "2026-06-15")
            String resultDecisionDate,
            @Schema(description = "판결결과(심결결과)", example = "청구인용")
            String resultDecisionResult,
            @Schema(description = "청구인", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo resultPetitioner,
            @Schema(description = "피청구인", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo resultRespondent,
            @Schema(description = "판결코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
            CommonRecordResponse.CodeInfo judgmentCategory,
            @Schema(description = "비고") String note
    ) {
        /**
         * [Helper] 화면 Record -> DB용 ConflictMstVO 변환
         * 서비스 임플(Impl)에서 매퍼 던지기 직전에 사용합니다.
         */
        public ConflictMstVO toVO(String officeSeq, String loginUser) {
            return ConflictMstVO.builder()
                    .officeSeq(officeSeq)
                    .conflictSeq(this.conflictSeq())
                    .litigationCaseNo(this.resultCaseNo())          // 화면(ResultCaseNo) -> DB(litigationCaseNo)
                    .decisionContent(this.resultDecisionResult())    // 화면(ResultDecisionResult) -> DB(decisionContent)

                    .note(this.note())
                    .createUser(loginUser)
                    .updateUser(loginUser)
                    .build();
        }
    }


    @Builder
    @Schema(description = "출원 기본 정보")
    public record ConflictAppBaseInfo(
            @Schema(description = "출원키", example = "APPMST20260000312") String appSeq,

            @Schema(description = "국가코드", example = "{ \"code\": \"KR\", \"codeName\": \"대한민국\" })", format = "CODE")
            CommonRecordResponse.CodeInfo countryCode,

            @Schema(description = "출원일", example = "20260223", format = "YYYYMMDD")
            String appDate,
            @Schema(description = "출원번호", example = "10-2026-1234567")
            String appNo,

            @Schema(description = "출원/등록공고일", example = "20260201")
            String announcementDate,

            @Schema(description = "등록일", example = "20260223", format = "YYYYMMDD")
            String regDate,
            @Schema(description = "등록번호", example = "10-1234567-0000")
            String regNo,

            @Schema(description = "청구마감일", example = "20260223", format = "YYYYMMDD")
            String dueLimitDate,

            @Schema(description = "청구일", example = "20260223", format = "YYYYMMDD")
            String claimDate,
            @Schema(description = "사건번호", example = "2026-당-1234")
            String caseNo,
            @Schema(description = "국내등록결정일")
            String domesticRegDecisionDate,
            @Schema(description = "국내등록일")
            String domesticRegDate,
            @Schema(description = "국내등록번호")
            String domesticRegNo,
            @Schema(description = "국제등록일")
            String intlRegDate







    ) {
        public static ConflictAppBaseInfo from(ConflictMergeVO vo) {
            return vo == null ? null : ConflictAppBaseInfo.builder()
                    .appSeq(vo.getAppSeq())
                    .countryCode(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getCountryCode())
                            .codeName(vo.getCountryCodeName())
                            .build())
                    .appDate(vo.getAppDate())
                    .appNo(vo.getAppNo())
                    .announcementDate(vo.getAnnouncementDate())
                    .regDate(vo.getRegDate())
                    .regNo(vo.getRegNo())
                    .dueLimitDate(vo.getDueLimitDate())
                    .claimDate(vo.getClaimDate())
                    .caseNo(vo.getCaseNo())
                    .domesticRegDecisionDate(vo.getDomesticRegDecisionDate())
                    .domesticRegDate(vo.getDomesticRegDate())
                    .domesticRegNo(vo.getDomesticRegNo())
                    .intlRegDate(vo.getIntlRegDate())
                    .build();


        }
    }

    @Builder
    @Schema(description = "물품 정보")
    public record ConflictAppGoodsInfo(
            @Schema(description = "물품류", example = "제09류") String goodsClass
    ) {
        public static ConflictAppGoodsInfo from(ConflictMergeVO vo) {
            return vo == null ? null : ConflictAppGoodsInfo.builder().goodsClass(vo.getGoodsClass()).build();
        }
    }

    @Builder
    @Schema(description = "당사자 정보")
    public record ConflictAppPartyInfo(

            @Schema(description = "해외대리인", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo foreignAgent,

            @Schema(description = "의뢰인", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo client,

            @Schema(description = "출원인", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo applicant

    ) {
        public static ConflictAppPartyInfo from(ConflictMergeVO vo) {
            return vo == null ? null : ConflictAppPartyInfo.builder()
                    .foreignAgent(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getForeignAgent())
                            .userName(vo.getForeignAgentName())
                            .build())
                    .client(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getClient())
                            .userName(vo.getClientName())
                            .build())
                    .applicant(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getApplicant())
                            .userName(vo.getApplicantName())
                            .build())
                    .build();
        }
    }

    @Builder
    @Schema(description = "명칭 정보")
    public record ConflictAppTitleInfo(
            @Schema(description = "국문명칭", example = "이차전지용 전극") String titleKo,
            @Schema(description = "영문명칭", example = "Electrode for Secondary Battery") String titleEn
    ) {
        public static ConflictAppTitleInfo from(ConflictMergeVO vo) {
            return vo == null ? null : ConflictAppTitleInfo.builder().titleKo(vo.getTitleKo()).titleEn(vo.getTitleEn()).build();
        }
    }

    @Builder
    @Schema(description = "심판 사건관리 정보")
    public record ConflictCftCaseMng(
            @Schema(description = "계류법정", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
            CommonRecordResponse.CodeInfo courtCategory,
            @Schema(description = "구분", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
            //CommonRecordResponse.CodeInfo caseCategory,
            CommonRecordResponse.CodeInfo appClassification,
//
            @Schema(description = "대리인구분", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
            CommonRecordResponse.CodeInfo agentCategory,

            @Schema(description = "사건종류구분", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
            CommonRecordResponse.CodeInfo caseType,

            @Schema(description = "권리", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
            CommonRecordResponse.CodeInfo rightType,

//            @Schema(description = "현재상태", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
//            CommonRecordResponse.CodeInfo status,

            @CommonMapping(type = "DATE", group = "CFT", code = "receiptDate", description = "접수일")
            @Schema(description = "접수일", example = "20260223", format = "YYYYMMDD")
            String receiptDate,

            @Schema(description = "OurRef", example = "MP-2026-0001")
            String ourRef,

            @Schema(description = "YourRef", example = "CLIENT-REF-01")
            String yourRef,

            @Schema(description = "출원인관리번호", example = "C12345")
            String clientRef
    ) {
        public static ConflictCftCaseMng from(ConflictMergeVO vo) {
            return vo == null ? null : ConflictCftCaseMng.builder()
//                    .courtCategory(CommonRecordResponse.CodeInfo.builder()
//                            .code(vo.getCourtCategoryCode())
//                            .codeName(vo.getCourtCategoryCodeName())
//                            .build())
//                    .status(CommonRecordResponse.CodeInfo.builder()
//                            .code(vo.getStatusCode())
//                            .codeName(vo.getStatusCodeName())
//                            .build())
                    .agentCategory(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getAgentCategoryCode())
                            .codeName(vo.getAgentCategoryCodeName())
                            .build()
                            )
//                    .caseCategory(CommonRecordResponse.CodeInfo.builder()
//                            .code(vo.getCaseCategoryCode())
//                            .codeName(vo.getCaseCategoryCodeName())
//                            .build())
                    .appClassification(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getAppClassificationCode())
                            .codeName(vo.getAppClassificationCodeName())
                            .build())
                    .caseType(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getCaseTypeCode())
                            .codeName(vo.getCaseTypeCodeName())
                            .build())

                    .rightType(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getRightTypeCode())
                            .codeName(vo.getRightTypeCodeName())
                            .build())
                    .receiptDate(vo.getReceiptDate())
                    .ourRef(vo.getOurRef())
                    .yourRef(vo.getYourRef())
                    .clientRef(vo.getClientRef())
                    .build();
        }
    }
    @Builder
    @Schema(description = "기타사건 심판 사건관리 정보")
    public record ConflictEtcCftCaseMng(
//            @Schema(description = "계류법정", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
//            CommonRecordResponse.CodeInfo courtCategory,

//            @Schema(description = "대리인구분", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
//            CommonRecordResponse.CodeInfo agentCategory,

            @Schema(description = "출원키", example = "APPMST20260000312")
            String appSeq,

            @Schema(description = "사건종류구분", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
            CommonRecordResponse.CodeInfo caseType,

            @Schema(description = "국내외구분", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
            //CommonRecordResponse.CodeInfo caseCategory,
            CommonRecordResponse.CodeInfo appClassification,

            @Schema(description = "권리", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
            CommonRecordResponse.CodeInfo rightType,

//            @Schema(description = "현재상태", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
//            CommonRecordResponse.CodeInfo status,

            @CommonMapping(type = "DATE", group = "CFT", code = "receiptDate", description = "접수일")
            @Schema(description = "접수일", example = "20260223", format = "YYYYMMDD")
            String receiptDate,

            @Schema(description = "OurRef", example = "MP-2026-0001")
            String ourRef,

            @Schema(description = "YourRef", example = "CLIENT-REF-01")
            String yourRef,

            @Schema(description = "출원인관리번호", example = "C12345")
            String clientRef
    ) {
        public static ConflictEtcCftCaseMng from(ConflictMergeVO vo) {
            return vo == null ? null : ConflictEtcCftCaseMng.builder()
//                    .courtCategory(CommonRecordResponse.CodeInfo.builder()
//                            .code(vo.getCourtCategoryCode())
//                            .codeName(vo.getCourtCategoryCodeName())
//                            .build())
//                    .status(CommonRecordResponse.CodeInfo.builder()
//                            .code(vo.getStatusCode())
//                            .codeName(vo.getStatusCodeName())
//                            .build())
//                    .agentCategory(CommonRecordResponse.CodeInfo.builder()
//                            .code(vo.getAgentCategoryCode())
//                            .codeName(vo.getAgentCategoryCodeName())
//                            .build()
//                            )
                    .caseType(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getCaseTypeCode())
                            .codeName(vo.getCaseTypeCodeName())
                            .build())
//                    .caseCategory(CommonRecordResponse.CodeInfo.builder()
//                            .code(vo.getCaseCategoryCode())
//                            .codeName(vo.getCaseCategoryCodeName())
//                            .build())
                    .appClassification(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getAppClassificationCode())
                            .codeName(vo.getAppClassificationCodeName())
                            .build())
                    .rightType(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getRightTypeCode())
                            .codeName(vo.getRightTypeCodeName())
                            .build())
                    .receiptDate(vo.getReceiptDate())
                    .ourRef(vo.getOurRef())
                    .yourRef(vo.getYourRef())
                    .clientRef(vo.getClientRef())
                    .build();
        }
    }

    @Builder
    @Schema(description = "심판 판결 정보")
    public record ConflictCftJudgmentInfo(

            @Schema(description = "심사전치일", example = "20260223", format = "YYYYMMDD")
            String preExamDate,
            @Schema(description = "심사전치결과", example = "인용") String
            preExamResult,
            @Schema(description = "최종결과", example = "승소")
            String finalResult,

            @Schema(description = "보정서 마감일", example = "20260223", format = "YYYYMMDD")
            String amendLimitDate,

            @Schema(description = "보정서 제출일", example = "20260223", format = "YYYYMMDD")
            String amendSubmitDate,

            @Schema(description = "판결 송달일", example = "20260223", format = "YYYYMMDD")
            String judgmentServedDate,

            @Schema(description = "판결 결정일", example = "20260223", format = "YYYYMMDD")
            String judgmentDate,
            @Schema(description = "결정내용", example = "심판청구를 인용한다.")
            String decisionContent,
            @Schema(description = "불복제기 내용") String appealContent,

            @Schema(description = "불복제기 마감일", example = "20260223", format = "YYYYMMDD")
            String appealLimitDate,

            @Schema(description = "불복제기 청구일", example = "20260223", format = "YYYYMMDD")
            String appealDate,
            @Schema(description = "포기건 여부", example = "Y")
            String isAbandoned,

            @Schema(description = "포기 지시일자", example = "20260223", format = "YYYYMMDD")
            String abandonInstructDate,

            @Schema(description = "포기 일자", example = "20260223", format = "YYYYMMDD")
            String abandonDate,
            @Schema(description = "포기 내용", example = "실익 없음으로 인한 포기")
            String abandonContent
    ) {
        public static ConflictCftJudgmentInfo from(ConflictMergeVO vo) {
            return vo == null ? null : ConflictCftJudgmentInfo.builder()
                    .preExamDate(vo.getPreExamDate())
                    .preExamResult(vo.getPreExamResult())
                    .finalResult(vo.getFinalResult())
                    .amendLimitDate(vo.getAmendLimitDate())
                    .amendSubmitDate(vo.getAmendSubmitDate())
                    .judgmentServedDate(vo.getJudgmentServedDate())
                    .judgmentDate(vo.getJudgmentDate())
                    .decisionContent(vo.getDecisionContent())
                    .appealContent(vo.getAppealContent())
                    .appealLimitDate(vo.getAppealLimitDate())
                    .appealDate(vo.getAppealDate())
                    .isAbandoned(vo.getIsAbandoned())
                    .abandonInstructDate(vo.getAbandonInstructDate())
                    .abandonDate(vo.getAbandonDate())
                    .abandonContent(vo.getAbandonContent())
                    .build();
        }
    }

    @Builder
    @Schema(description = "담당 정보")
    public record ConflictCftManagerInfo(
            @Schema(description = "부서", example = "IP전략팀")
            String deptName,

            @Schema(description = "관리담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo adminMgr,

            @Schema(description = "사건담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo caseMgr,

            @Schema(description = "담당변리사", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo attorney
    ) {
        public static ConflictCftManagerInfo from(ConflictMergeVO vo) {
            return vo == null ? null : ConflictCftManagerInfo.builder()
                    .deptName(vo.getDeptName())
                    .adminMgr(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getAdminMgr())
                            .userName(vo.getAdminMgrName())
                            .build())
                    .caseMgr(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getCaseMgr())
                            .userName(vo.getCaseMgrName())
                            .build())
                    .attorney(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getAttorney())
                            .userName(vo.getAttorneyName())
                            .build())
                    .build();
        }
    }

    @Builder
    @Schema(description = "관계자 정보")
    public record ConflictCftLitigantInfo(

            @Schema(description = "소개자", example = "지인소개")
            String introducer,
            @Schema(description = "청구인 원고/피고", example = "원고")
            String petitionerType,

            @Schema(description = "청구인명", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo petitioner,
            @Schema(description = "청구인 메모", example = "우선권 주장 확인 필요")
            String petitionerMemo,
            @Schema(description = "피청구인 원고/피고", example = "피고")
            String respondentType,

            @Schema(description = "피청구인명", example = "홍길동")
            String respondent,
            @Schema(description = "피청구인 메모", example = "대응 논리 준비중")
            String respondentMemo
    ) {
        public static ConflictCftLitigantInfo from(ConflictMergeVO vo) {
            return vo == null ? null : ConflictCftLitigantInfo.builder()
                    .introducer(vo.getIntroducer())
                    .petitionerType(vo.getPetitionerType())
                    .petitioner(CommonRecordResponse.PersonInfo.builder()
                                    .userSeq(vo.getPetitioner())
                                    .userName(vo.getPetitionerName())
                                    .build()
                                )
                    .petitionerMemo(vo.getPetitionerMemo())
                    .respondentType(vo.getRespondentType())
                    .respondent(vo.getRespondentName())
                    .respondentMemo(vo.getRespondentMemo())
                    .build();
        }
    }

    @Builder
    public record ConflictResultList(
            // 여기서 ConflictResultDetail(Record) 리스트를 받음
            List<ConflictRequest.ConflictResultDetail> conflictResultList
    ) {
    }

    @Builder
    @Schema(description = "심판 결과 상세 정보 (심급별)")
    public record ConflictResultDetail(
//            @Schema(description = "분쟁 일련번호", example = "CFT20260129-0001")
//            String conflictSeq,
//
//            @Schema(description = "사무소 일련번호", example = "OFFICE001")
//            String officeSeq,

//            @Schema(description = "심판 결과 일련번호", example = "RES202600123")
//            String conflictResultSeq,

            @Schema(description = "사건번호", example = "2026당123456")
            String judgmentCaseNo,

            @Schema(description = "판결/심결 내용", example = "심판청구를 인용한다.")
            String judgmentContent,

            @Schema(description = "심결문 조회 URL", example = "https://kportal.kipris.or.kr/...")
            String judgmentSearchUrl,

            @Schema(description = "판결/종결 상태 코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
            CommonRecordResponse.CodeInfo judgmentCategory,

            @Schema(description = "판결일 (YYYYMMDD)", example = "20260223", format = "YYYYMMDD")
            String resultDecisionDate,

            String note
    ) {
    }


    @Builder
    @Schema(description = "비고")
    public record ConflictCftNoteInfo(
            @Schema(description = "비고", example = "2026년 중요 관리 건") String note
    ) {
        public static ConflictCftNoteInfo from(ConflictMergeVO vo) {
            return vo == null ? null : ConflictCftNoteInfo.builder().note(vo.getNote()).build();
        }
    }
//기타사건 출원기본정보
    public record EtcAppBaseInfo(
            @Schema(description = "출원키", example = "APPMST20260000312") String appSeq,

            @Schema(description = "국가코드", example = "{ \"code\": \"KR\", \"codeName\": \"대한민국\" })", format = "CODE")
            CommonRecordResponse.CodeInfo countryCode,
            @CommonMapping(type = "DATE", group = "APP", code = "", description = "출원일")
            @Schema(description = "출원일", example = "20260223", format = "YYYYMMDD")
            String appDate,
            @Schema(description = "출원번호", example = "10-2026-1234567")
            String appNo,

//            @Schema(description = "출원/등록공고일", example = "20260223", format = "YYYYMMDD")
//            String announcementDate,

            @Schema(description = "등록일", example = "20260223", format = "YYYYMMDD")
            String regDate,
            @Schema(description = "등록번호", example = "10-1234567-0000")
            String regNo,

            @Schema(description = "청구마감일", example = "20260223", format = "YYYYMMDD")
            String dueLimitDate,

            @Schema(description = "청구일", example = "20260223", format = "YYYYMMDD")
            String claimDate,

            // 기타사건 전용
            @Schema(description = "국내등록결정일",example = "20260223", format = "YYYYMMDD")
            String domesticRegDecisionDate,
            @Schema(description = "국내등록일",example = "20260223", format = "YYYYMMDD")
            String domesticRegDate,
            @Schema(description = "국내등록번호",example = "10-1234567-0000")
            String domesticRegNo,
            @Schema(description = "국제등록일")
            String intlRegDate,
            @Schema(description = "처리일",example = "20260223", format = "YYYYMMDD")
            String processDate
    ) {}
    @Builder
    @Schema(description = "기타사건 관계자 정보")
    public record ConflictEtcLitigantInfo(
            //@Schema(description = "사건번호", example = "2026-당-1234") String caseNo,

            @Schema(description = "사건명", example = "상표권 침해 금지 가처분")
            String caseTitleKo,


            @Schema(description = "소개자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            String introducer,

            @Schema(description = "청구인 원고/피고", example = "원고")
            String petitionerType,


            @Schema(description = "청구인명", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo petitioner,

            @Schema(description = "청구인 메모", example = "청구인측 증거 자료 보강 필요")
            String petitionerMemo,

            @Schema(description = "피청구인 원고/피고", example = "피고")
            String respondentType,


            @Schema(description = "피청구인명", example = "홍길동" )
            String respondent,

            @Schema(description = "피청구인 메모", example = "상대방 대리인 선임 확인됨")
            String respondentMemo
    ) {
        public static ConflictEtcLitigantInfo from(ConflictMergeVO vo) {
            if (vo == null) return null;

            return ConflictEtcLitigantInfo.builder()
                    .caseTitleKo(vo.getCaseTitleKo()) // 사건명 추가
                    .introducer(vo.getIntroducer())
                    .petitionerType(vo.getPetitionerType())
                    .petitioner(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getPetitioner())
                            .userName(vo.getPetitionerName())
                            .build())
                    .petitionerMemo(vo.getPetitionerMemo())
                    .respondentType(vo.getRespondentType())
                    .respondent(vo.getRespondent())
                    .respondentMemo(vo.getRespondentMemo())
                    .build();
        }
    }
    @Builder
    @Schema(description = "당사자 정보")
    public record ConflictEtcAppPartyInfo(

            @Schema(description = "해외대리인", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo foreignAgent,

            @Schema(description = "의뢰인")
            CommonRecordResponse.PersonInfo client,

            @Schema(description = "출원인", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo applicant

    ) {
        public static ConflictAppPartyInfo from(ConflictMergeVO vo) {
            return vo == null ? null : ConflictAppPartyInfo.builder()
                    .foreignAgent(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getForeignAgent())
                            .userName(vo.getForeignAgentName())
                            .build())
                    .applicant(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getApplicant())
                            .userName(vo.getApplicantName())
                            .build())
                    .client(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getClient())
                            .userName(vo.getClientName())
                            .build())
                    .build();
        }
    }

    @Builder
    @Schema(description = "기타사건 상세 요청/응답 데이터")
    public record ConflictEtcCaseDetail(
            @Schema(description = "분쟁 일련번호" , example = "CFTMST20260000118", format = "SEQ") String conflictSeq,

            /* 하위 레코드는 기존 ConflictRequest 것을 100% 재사용 */
            ConflictRequest.ConflictEtcCftCaseMng cftCaseMng,       // 구분, 권리, 접수일, OurRef, YourRef, 출원인관리번호 등
            ConflictRequest.EtcAppBaseInfo appBaseInfo,     // 국가, 출원/등록일 등 + (아래 필드들 포함하도록 확장)
            ConflictRequest.ConflictAppTitleInfo appTitleInfo,   // 명칭
            ConflictRequest.ConflictAppGoodsInfo appGoodsInfo,   // 물품류
            ConflictRequest.ConflictEtcAppPartyInfo appPartyInfo,   // 해외대리인, 의뢰인, 출원인
            ConflictRequest.ConflictEtcLitigantInfo cftLitigantInfo,
            ConflictRequest.ConflictCftJudgmentInfo cftJudgmentInfo, // 처리마감일, 처리일, 최종결과 등
            ConflictRequest.ConflictCftManagerInfo cftManagerInfo,   // 부서, 담당자 등
            ConflictRequest.ConflictCftNoteInfo cftNoteInfo


            ) {
    }



}