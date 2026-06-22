package kr.co.mindpro.ipms.domain.conflict.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.util.CommonMapping;

import kr.co.mindpro.ipms.domain.conflict.dto.request.ConflictRequest;
import kr.co.mindpro.ipms.domain.conflict.dto.request.ConflictRequest.*;
import kr.co.mindpro.ipms.domain.conflict.vo.ConflictMergeVO;
import kr.co.mindpro.ipms.domain.conflict.vo.ConflictMstVO;

import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;

import lombok.Builder;


import java.util.List;

/**
 * 분쟁/심판 상세 응답 통합 DTO
 */
@Schema(description = "분쟁/심판 응답 통합 클래스")
public class ConflictResponse {

    /**
     * 상세 조회용 통합 Record
     */


        @Builder
        @Schema(description = "분쟁/심판 상세 응답")
        public record ConflictDetail(
                @Schema(description = "분쟁/심판 일련번호", example = "CFL20260001")
                String conflictSeq,
                ConflictCftCaseMng cftCaseMng,
                ConflictAppBaseInfo appBaseInfo,
                ConflictCftManagerInfo cftManagerInfo,
                ConflictAppPartyInfo appPartyInfo,
                ConflictAppTitleInfo appTitleInfo,
                ConflictAppGoodsInfo appGoodsInfo,
                ConflictCftLitigantInfo cftLitigantInfo,
                ConflictCftJudgmentInfo cftJudgmentInfo,
                ConflictFiles cftFileList,
                ConflictCftNoteInfo cftNoteInfo,
                ConflictResultList cftResultList
        ) {
            public static ConflictDetail of(ConflictMstVO mstVO, ConflictMergeVO mergeVO, List<PaperResponseVO> fileList, ConflictResultList resultList) {
                return ConflictDetail.builder()
                        .conflictSeq(mstVO.getConflictSeq())
                        .cftCaseMng(ConflictCftCaseMng.from(mergeVO))
                        .appBaseInfo(ConflictAppBaseInfo.from(mergeVO))
                        .cftManagerInfo(ConflictCftManagerInfo.from(mergeVO))
                        .appPartyInfo(ConflictAppPartyInfo.from(mergeVO))
                        .appTitleInfo(ConflictAppTitleInfo.from(mergeVO))
                        .appGoodsInfo(ConflictAppGoodsInfo.from(mergeVO))
                        .cftLitigantInfo(ConflictCftLitigantInfo.from(mergeVO))
                        .cftJudgmentInfo(ConflictCftJudgmentInfo.from(mergeVO))
                        .cftNoteInfo(ConflictCftNoteInfo.from(mergeVO))
                        .cftFileList(ConflictFiles.from(fileList))
                        .cftResultList(resultList)
                        .build();
            }
        }

        @Builder
        @Schema(description = "심판 사건관리")
        public record ConflictCftCaseMng(
                @Schema(description = "출원키", example = "APPMST20260000128") String appSeq,
                @Schema(description = "계류법정", example = "{ \"code\": \"10\", \"codeName\": \"특허심판원\" }", format = "CODE")
                CommonRecordResponse.CodeInfo courtCategory,
                @Schema(description = "대리인구분", example = "{ \"code\": \"10\", \"codeName\": \"쌍방대리\" }", format = "CODE")
                CommonRecordResponse.CodeInfo agentCategory,
                @Schema(description = "사건종류구분", example = "{ \"code\": \"10\", \"codeName\": \"무효심판\" }", format = "CODE")
                CommonRecordResponse.CodeInfo caseType,
                @Schema(description = "국내외구분", example = "{ \"code\": \"KR\", \"codeName\": \"국내\" }", format = "CODE")
                //CommonRecordResponse.CodeInfo caseCategory,
                CommonRecordResponse.CodeInfo appClassification,
                @Schema(description = "권리", example = "{ \"code\": \"10\", \"codeName\": \"특허\" }", format = "CODE")
                CommonRecordResponse.CodeInfo rightType,
                @Schema(description = "현재상태", example = "{ \"code\": \"10\", \"codeName\": \"진행중\" }", format = "CODE")
                CommonRecordResponse.CodeInfo status,
                @Schema(description = "접수일", example = "20260223", format = "YYYYMMDD") String receiptDate,
                @Schema(description = "OurRef", example = "MP-2026-0001") String ourRef,
                @Schema(description = "YourRef", example = "CLIENT-REF-01") String yourRef,
                @Schema(description = "출원인관리번호", example = "C12345") String clientRef
        ) {
            public static ConflictCftCaseMng from(ConflictMergeVO vo) {
                if (vo == null) return null;
                return ConflictCftCaseMng.builder()
                        .appSeq(vo.getAppSeq())
                        .courtCategory(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getCourtCategoryCode())
                                .codeName(vo.getCourtCategoryCodeName())
                                .build())
                        .agentCategory(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getAgentCategoryCode())
                                .codeName(vo.getAgentCategoryCodeName())
                                .build())
                        .caseType(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getCaseTypeCode())
                                .codeName(vo.getCaseTypeCodeName())
                                .build())
//                        .caseCategory(CommonRecordResponse.CodeInfo.builder()
//                                .code(vo.getCaseCategoryCode())
//                                .codeName(vo.getCaseCategoryCodeName())
//                                .build())
                        .appClassification(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getAppClassificationCode())
                                .codeName(vo.getAppClassificationCodeName())
                                .build())
                        .rightType(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getRightTypeCode())
                                .codeName(vo.getRightTypeCodeName())
                                .build())
                        .status(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getStatusCode())
                                .codeName(vo.getStatusCodeName())
                                .build())
                        .receiptDate(vo.getReceiptDate())
                        .ourRef(vo.getOurRef())
                        .yourRef(vo.getYourRef())
                        .clientRef(vo.getClientRef())
                        .build();
            }
        }

    @Builder
    @Schema(description = "심판 사건관리")
    public record ConflictEtcCftCaseMng(
            @Schema(description = "출원키", example = "APPMST20260000128") String appSeq,
//                @Schema(description = "계류법정", example = "{ \"code\": \"10\", \"codeName\": \"특허심판원\" }", format = "CODE")
//                CommonRecordResponse.CodeInfo courtCategory,
//            @Schema(description = "대리인구분", example = "{ \"code\": \"10\", \"codeName\": \"쌍방대리\" }", format = "CODE")
//            CommonRecordResponse.CodeInfo agentCategory,
            @Schema(description = "사건종류구분", example = "{ \"code\": \"10\", \"codeName\": \"무효심판\" }", format = "CODE")
            CommonRecordResponse.CodeInfo caseType,
            @Schema(description = "국내외구분", example = "{ \"code\": \"KR\", \"codeName\": \"국내\" }", format = "CODE")
            CommonRecordResponse.CodeInfo appClassification,
            @Schema(description = "권리", example = "{ \"code\": \"10\", \"codeName\": \"특허\" }", format = "CODE")
            CommonRecordResponse.CodeInfo rightType,
            @Schema(description = "현재상태", example = "{ \"code\": \"10\", \"codeName\": \"진행중\" }", format = "CODE")
            CommonRecordResponse.CodeInfo status,
            @Schema(description = "접수일", example = "20260223", format = "YYYYMMDD") String receiptDate,
            @Schema(description = "OurRef", example = "MP-2026-0001") String ourRef,
            @Schema(description = "YourRef", example = "CLIENT-REF-01") String yourRef,
            @Schema(description = "출원인관리번호", example = "C12345") String clientRef
    ) {
        public static ConflictEtcCftCaseMng from(ConflictMergeVO vo) {
            if (vo == null) return null;
            return ConflictEtcCftCaseMng.builder()
                    .appSeq(vo.getAppSeq())
//                        .courtCategory(CommonRecordResponse.CodeInfo.builder()
//                                .code(vo.getCourtCategoryCode())
//                                .codeName(vo.getCourtCategoryCodeName())
//                                .build())
//                    .agentCategory(CommonRecordResponse.CodeInfo.builder()
//                            .code(vo.getAgentCategoryCode())
//                            .codeName(vo.getAgentCategoryCodeName())
//                            .build())
                    .caseType(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getCaseTypeCode())
                            .codeName(vo.getCaseTypeCodeName())
                            .build())
                    .appClassification(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getAppClassificationCode())
                            .codeName(vo.getAppClassificationCodeName())
                            .build())
                    .rightType(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getRightTypeCode())
                            .codeName(vo.getRightTypeCodeName())
                            .build())
                    .status(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getStatusCode())
                            .codeName(vo.getStatusCodeName())
                            .build())
                    .receiptDate(vo.getReceiptDate())
                    .ourRef(vo.getOurRef())
                    .yourRef(vo.getYourRef())
                    .clientRef(vo.getClientRef())
                    .build();
        }
    }

        @Builder
        @Schema(description = "출원 기본 정보")
        public record ConflictAppBaseInfo(
                @Schema(description = "국가코드", example = "{ \"code\": \"KR\", \"codeName\": \"대한민국\" }", format = "CODE")
                CommonRecordResponse.CodeInfo countryCode,
                @Schema(description = "출원일", example = "20260223", format = "YYYYMMDD") String appDate,
                @Schema(description = "출원번호", example = "10-2026-1234567") String appNo,
                @Schema(description = "출원/등록공고일", example = "20260201") String announcementDate,
                @Schema(description = "등록일", example = "20260223", format = "YYYYMMDD") String regDate,
                @Schema(description = "등록번호", example = "10-1234567-0000") String regNo,
                @Schema(description = "청구마감일", example = "20260223", format = "YYYYMMDD") String dueLimitDate,
                @Schema(description = "청구일", example = "20260223", format = "YYYYMMDD") String claimDate,
                @Schema(description = "사건번호", example = "2026-당-1234") String caseNo,
                @Schema(description = "국내등록결정일") String domesticRegDecisionDate,
                @Schema(description = "국내등록일") String domesticRegDate,
                @Schema(description = "국내등록번호") String domesticRegNo,
                @Schema(description = "국제등록일") String intlRegDate
        ) {
            public static ConflictAppBaseInfo from(ConflictMergeVO vo) {
                if (vo == null) return null;
                return ConflictAppBaseInfo.builder()
                        .countryCode(CommonRecordResponse.CodeInfo.builder().code(vo.getCountryCode()).codeName(vo.getCountryCodeName()).build())
                        .appDate(vo.getAppDate()).appNo(vo.getAppNo()).announcementDate(vo.getAnnouncementDate())
                        .regDate(vo.getRegDate()).regNo(vo.getRegNo()).dueLimitDate(vo.getDueLimitDate())
                        .claimDate(vo.getClaimDate()).caseNo(vo.getCaseNo())
                        .domesticRegDecisionDate(vo.getDomesticRegDecisionDate())
                        .domesticRegDate(vo.getDomesticRegDate())
                        .domesticRegNo(vo.getDomesticRegNo())
                        .intlRegDate(vo.getIntlRegDate())
                        .build();
            }
        }

        @Builder
        @Schema(description = "당사자 정보")
        public record ConflictAppPartyInfo(
                @Schema(description = "해외대리인", example = "{ \"userSeq\": \"USERIF2026001\", \"userName\": \"Global IP\" }")
                CommonRecordResponse.PersonInfo foreignAgent,
                @Schema(description = "의뢰인", example = "{ \"userSeq\": \"USERIF2026002\", \"userName\": \"삼성전자\" }")
                CommonRecordResponse.PersonInfo client,
                @Schema(description = "출원인", example = "{ \"userSeq\": \"USERIF2026003\", \"userName\": \"홍길동\" }")
                CommonRecordResponse.PersonInfo applicant
        ) {
            public static ConflictAppPartyInfo from(ConflictMergeVO vo) {
                if (vo == null) return null;
                return ConflictAppPartyInfo.builder()
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
        @Schema(description = "관계자 정보")
        public record ConflictCftLitigantInfo(
                @Schema(description = "소개자", example = "{ \"userSeq\": \"USER2026001\", \"userName\": \"홍길동\" }")
                String introducer,
                @Schema(description = "청구인 원고/피고", example = "원고")
                String petitionerType,
                @Schema(description = "청구인명", example = "{ \"userSeq\": \"USER2026001\", \"userName\": \"홍길동\" }")
                CommonRecordResponse.PersonInfo petitioner,
                @Schema(description = "청구인 메모") String petitionerMemo,
                @Schema(description = "피청구인 원고/피고", example = "피고") String respondentType,
                @Schema(description = "피청구인명", example = "{ \"userSeq\": \"USER2026002\", \"userName\": \"김철수\" }")
                String respondent,
                @Schema(description = "피청구인 메모") String respondentMemo


        ) {
            public static ConflictCftLitigantInfo from(ConflictMergeVO vo) {
                if (vo == null) return null;
                return ConflictCftLitigantInfo.builder()
                        .introducer(vo.getIntroducer())
                        .petitionerType(vo.getPetitionerType())
                        .petitioner(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getPetitioner()).userName(vo.getPetitionerName()).build())
                        .petitionerMemo(vo.getPetitionerMemo())
                        .respondentType(vo.getRespondentType())
                        .respondent(vo.getRespondent())
                        .respondentMemo(vo.getRespondentMemo())
                        .build();
            }
        }

        @Builder
        @Schema(description = "담당 정보")
        public record ConflictCftManagerInfo(
                @Schema(description = "부서", example = "IP전략팀") String deptName,
                @Schema(description = "관리담당자", example = "{ \"userSeq\": \"USERIF2026001\", \"userName\": \"홍길동\" }")
                CommonRecordResponse.PersonInfo adminMgr,
                @Schema(description = "사건담당자", example = "{ \"userSeq\": \"USERIF2026002\", \"userName\": \"김철수\" }")
                CommonRecordResponse.PersonInfo caseMgr,
                @Schema(description = "담당변리사", example = "{ \"userSeq\": \"USERIF2026003\", \"userName\": \"이영희\" }")
                CommonRecordResponse.PersonInfo attorney
        ) {
            public static ConflictCftManagerInfo from(ConflictMergeVO vo) {
                if (vo == null) return null;
                return ConflictCftManagerInfo.builder()
                        .deptName(vo.getDeptName())
                        .adminMgr(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getAdminMgr()).userName(vo.getAdminMgrName()).build())
                        .caseMgr(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getCaseMgr()).userName(vo.getCaseMgrName()).build())
                        .attorney(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getAttorney()).userName(vo.getAttorneyName()).build())
                        .build();
            }
        }

        @Builder
        @Schema(description = "심판 판결 정보")
        public record ConflictCftJudgmentInfo(
                @Schema(description = "심사전치일", example = "20260223", format = "YYYYMMDD") String preExamDate,
                @Schema(description = "심사전치결과")
                String preExamResult,
                @Schema(description = "최종결과")
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
                if (vo == null) return null;
                return ConflictCftJudgmentInfo.builder()
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
        @Schema(description = "첨부파일 목록")
        public record ConflictFiles(
                @Schema(description = "파일 리스트")
                List<CommonRecordResponse.FileInfo> fileList
        ) {
            public static ConflictFiles from(List<PaperResponseVO> list) {
                return ConflictFiles.builder()
                        .fileList(CommonRecordResponse.FileInfo.from(list))
                        .build();
            }
        }

        @Builder
        public record ConflictResultList(
                @Schema(description = "심판 결과 상세 리스트")
                List<ConflictResultDetail> conflictResultList
        ) {}

        @Builder
        public record ConflictResultDetail(
                @Schema(description = "분쟁 일련번호") String conflictSeq,
                @Schema(description = "분쟁 일련번호") String conflictResultSeq,
                @Schema(description = "사건번호", example = "2026당123456") String judgmentCaseNo,
                @Schema(description = "판결/심결 내용") String judgmentContent,
                @Schema(description = "심결문 조회 URL") String judgmentSearchUrl,
                @Schema(description = "판결코드", format = "CODE") CommonRecordResponse.CodeInfo judgmentCategory,
                @Schema(description = "판결일", example = "20260223", format = "YYYYMMDD") String resultDecisionDate,
                @Schema(description = "비고", example = "비고내용") String note

        ) {}

        @Builder
        @Schema(description = "명칭 정보")
        public record ConflictAppTitleInfo(
                @Schema(description = "국문명칭", example = "이차전지") String titleKo,
                @Schema(description = "영문명칭", example = "Battery") String titleEn
        ) {
            public static ConflictAppTitleInfo from(ConflictMergeVO vo) {
                return vo == null ? null : ConflictAppTitleInfo.builder().titleKo(vo.getTitleKo()).titleEn(vo.getTitleEn()).build();
            }
        }

        @Builder
        @Schema(description = "물품 정보")
        public record ConflictAppGoodsInfo(
                @Schema(description = "물품류", example = "09류") String goodsClass
        ) {
            public static ConflictAppGoodsInfo from(ConflictMergeVO vo) {
                return vo == null ? null : ConflictAppGoodsInfo.builder().goodsClass(vo.getGoodsClass()).build();
            }
        }

        @Builder
        @Schema(description = "비고")
        public record ConflictCftNoteInfo(
                @Schema(description = "비고내용", example = "특이사항 없음") String note
        ) {
            public static ConflictCftNoteInfo from(ConflictMergeVO vo) {
                return vo == null ? null : ConflictCftNoteInfo.builder().note(vo.getNote()).build();
            }
        }


    @Builder
    @Schema(description = "분쟁/심판 목록 개별 상세 정보")
    public record ConflictListDetail(
            @Schema(description = "분쟁 일련번호", example = "CFT2026000001")
            String conflictSeq,
            @Schema(description = "구분(국내외구분)", example = "내국")
            CommonRecordResponse.CodeInfo appClassification,
            @Schema(description = "OurRef (사건관리번호)", example = "OUR-2026-001")
            String ourRef,
            @Schema(description = "YourRef (고객관리번호)", example = "YOUR-999")
            String yourRef,
            @Schema(description = "현재상태", example = "진행중")
            CommonRecordResponse.CodeInfo status,
            @Schema(description = "사건종류", example = "무효심판")
            CommonRecordResponse.CodeInfo caseType,
            @Schema(description = "계류법정", example = "특허심판원")
            CommonRecordResponse.CodeInfo courtCategoryCode,
            @Schema(description = "사건번호", example = "2026당123456")
            String caseNo,
            @Schema(description = "대리인구분", example = "내자")
            CommonRecordResponse.CodeInfo agentCategoryCode,
            @Schema(description = "비고")
            String note,

            // --- [2. 날짜 및 판결 정보] ---
            @Schema(description = "접수일", example = "20260116")
            String receiptDate,
            @Schema(description = "사건마감일(청구마감일)", example = "20261231")
            String dueLimitDate,
            @Schema(description = "청구일", example = "20260110")
            String claimDate,
            @Schema(description = "판결결정일", example = "20260520")
            String judgmentDate,
            @Schema(description = "판결내용", example = "심판청구를 인용한다.")
            String decisionContent,
            @Schema(description = "불복마감일", example = "20260620")
            String appealLimitDate,
            @Schema(description = "불복제기일", example = "20260615")
            String appealDate,
            @Schema(description = "포기취하일", example = "20260701")
            String abandonDate,
            @Schema(description = "포기건 여부", example = "N")
            String isAbandoned,
            @Schema(description = "포기내용", example = "실익 없음")
            String abandonContent,

            // --- [3. 관계자 정보] ---
            @Schema(description = "의뢰인")
            CommonRecordResponse.PersonInfo client,
            @Schema(description = "해외대리인")
            CommonRecordResponse.PersonInfo foreignAgent,
            @Schema(description = "청구인")
            CommonRecordResponse.PersonInfo petitioner,
            @Schema(description = "피청구인")
            String respondent,
            @Schema(description = "관리담당자")
            CommonRecordResponse.PersonInfo adminMgr,
            @Schema(description = "사건담당자")
            CommonRecordResponse.PersonInfo caseMgr,
            @Schema(description = "담당변리사")
            CommonRecordResponse.PersonInfo attorney,

            // --- [4. 연동 출원 정보] ---
            @Schema(description = "국가코드")
            CommonRecordResponse.CodeInfo country,
            @Schema(description = "출원국가명", example = "미국")
            String appCountry,
            @Schema(description = "권리구분")
            CommonRecordResponse.CodeInfo rightType,
            @Schema(description = "출원번호", example = "10-2025-1234567")
            String appNo,
            @Schema(description = "출원일", example = "20251201")
            String appDate,
            @Schema(description = "등록번호", example = "10-1234567-0000")
            String regNo,
            @Schema(description = "등록일", example = "20260601")
            String regDate,
            @Schema(description = "국문명칭", example = "차세대 반도체 장치")
            String titleKo
    ) {}


        // --- 기타사건 관련 ---

    @Builder
    @Schema(description = "기타사건 목록 개별 상세 정보")
    public record ConflictEtcListDetail(
            // --- [1. 사건 기본 정보] ---
            @Schema(description = "기타사건 일련번호", example = "CFT2026000001")
            String conflictSeq,
            @Schema(description = "구분(국내외구분)", example = "내국")
            CommonRecordResponse.CodeInfo appClassification,
            @Schema(description = "OurRef (사건관리번호)", example = "OUR-ETC-2026-001")
            String ourRef,
            @Schema(description = "YourRef (고객관리번호)", example = "Y-ETC-999")
            String yourRef,
            @Schema(description = "현재상태", example = "진행중")
            CommonRecordResponse.CodeInfo status,
            @Schema(description = "사건종류", example = "경고장대응")
            CommonRecordResponse.CodeInfo caseType,
            @Schema(description = "사건명(기타사건 명칭)", example = "상표권 침해 조사")
            String caseTitleKo,
            @Schema(description = "비고")
            String note,

            // --- [2. 날짜 및 결과 정보] ---
            @Schema(description = "접수일", example = "20260116")
            String receiptDate,
            @Schema(description = "처리마감일", example = "20260331")
            String dueLimitDate,
            @Schema(description = "처리일", example = "20260325")
            String processDate,
            @Schema(description = "사건마감일", example = "20261231")
            String deadlineDate,
            @Schema(description = "최종결과", example = "합의종결")
            String finalResult,
            @Schema(description = "포기취하일", example = "20260701")
            String abandonDate,
            @Schema(description = "포기내용")
            String abandonContent,

            // --- [3. 관계자 정보] ---
            @Schema(description = "의뢰인")
            CommonRecordResponse.PersonInfo client,
            @Schema(description = "상대방", example = "경쟁업체 A사")
            String respondent,
            @Schema(description = "해외대리인")
            CommonRecordResponse.PersonInfo foreignAgent,
            @Schema(description = "관리담당자")
            CommonRecordResponse.PersonInfo adminMgr,
            @Schema(description = "사건담당자")
            CommonRecordResponse.PersonInfo caseMgr,
            @Schema(description = "담당변리사")
            CommonRecordResponse.PersonInfo attorney,

            // --- [4. 연동 출원/권리 정보] ---
            @Schema(description = "권리(종류)")
            CommonRecordResponse.CodeInfo rightType,
            @Schema(description = "출원번호")
            String appNo,
            @Schema(description = "출원일")
            String appDate,
            @Schema(description = "등록번호")
            String regNo,
            @Schema(description = "등록일")
            String regDate,
            @Schema(description = "국문명칭")
            String titleKo,
            @Schema(description = "영문명칭")
            String titleEn
    ) {}



        @Builder
        public record EtcAppBaseInfo(
                @Schema(description = "출원키", example = "APPMST20260000128") String appSeq,
                @Schema(description = "국가코드", example = "KR") CommonRecordResponse.CodeInfo countryCode,
                @Schema(description = "출원일", example = "20260101", format = "YYYYMMDD") String appDate,
                @Schema(description = "출원번호", example = "10-2026-1234567") String appNo,
                @Schema(description = "공고일", example = "20260201") String announceDate,
                @Schema(description = "등록일", example = "20260301", format = "YYYYMMDD") String regDate,
                @Schema(description = "등록번호", example = "10-1234567-0000") String regNo,
                @Schema(description = "청구마감일", example = "20260401", format = "YYYYMMDD") String dueLimitDate,
                @Schema(description = "처리일",example = "20260223", format = "YYYYMMDD")
                String processDate,
               // @Schema(description = "사건번호", example = "2026-당-1234") String caseNo,
                @Schema(description = "국내등록결정일") String domesticRegDecisionDate,
                @Schema(description = "국내등록일") String domesticRegDate,
                @Schema(description = "국내등록번호") String domesticRegNo,
                @Schema(description = "국제등록일") String intlRegDate
        ) {
            public static EtcAppBaseInfo from(ConflictMergeVO vo) {
                if (vo == null) return null;
                return EtcAppBaseInfo.builder()
                        .appSeq(vo.getAppSeq())
                        .countryCode(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getCountryCode())
                                .codeName(vo.getCountryCodeName())
                                .build())
                        .appDate(vo.getAppDate())
                        .appNo(vo.getAppNo()).announceDate(vo.getAnnouncementDate()).regDate(vo.getRegDate())
                        .regNo(vo.getRegNo()).dueLimitDate(vo.getDueLimitDate()).processDate(vo.getProcessDate())
                 //       .caseNo(vo.getCaseNo())
                        .domesticRegDecisionDate(vo.getDomesticRegDecisionDate())
                        .domesticRegDate(vo.getDomesticRegDate())
                        .domesticRegNo(vo.getDomesticRegNo())
                        .intlRegDate(vo.getIntlRegDate())
                        .build();
            }
        }

    @Builder
    @Schema(description = "기타사건 관계자 정보")
    public record ConflictEtcCftLitigantInfo(
            @Schema(description = "소개자", example = "{ \"userSeq\": \"USER2026001\", \"userName\": \"홍길동\" }")
            String introducer,
            @Schema(description = "청구인 원고/피고", example = "원고") String petitionerType,
            @Schema(description = "청구인명", example = "{ \"userSeq\": \"USER2026001\", \"userName\": \"홍길동\" }")
            CommonRecordResponse.PersonInfo petitioner,
            @Schema(description = "청구인 메모") String petitionerMemo,
            @Schema(description = "피청구인 원고/피고", example = "피고") String respondentType,
            @Schema(description = "피청구인명", example = "{ \"userSeq\": \"USER2026002\", \"userName\": \"김철수\" }")
            String respondent,
            @Schema(description = "피청구인 메모") String respondentMemo,
            //기타사건용

            @Schema(description = "사건번호", example = "2026-당-1234") String caseTitleKo

    ) {
        public static ConflictEtcCftLitigantInfo from(ConflictMergeVO vo) {
            if (vo == null) return null;
            return ConflictEtcCftLitigantInfo.builder()
                    .introducer(vo.getIntroducer())
                    .petitionerType(vo.getPetitionerType())
                    .petitioner(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getPetitioner()).userName(vo.getPetitionerName()).build())
                    .petitionerMemo(vo.getPetitionerMemo())
                    .respondentType(vo.getRespondentType())
                    .respondent(vo.getRespondent())
                    .respondentMemo(vo.getRespondentMemo())
                    //기타사건용
                    .caseTitleKo(vo.getCaseTitleKo())
                    .build();
        }
    }
    @Builder
    @Schema(description = "기타사건 당사자 정보")
    public record ConflictEtcAppPartyInfo(

            @Schema(description = "해외대리인", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo foreignAgent,

            @Schema(description = "해외대리인", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo client,

            @Schema(description = "출원인", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo applicant

    ) {
        public static ConflictResponse.ConflictEtcAppPartyInfo from(ConflictMergeVO vo) {
            return vo == null ? null : ConflictResponse.ConflictEtcAppPartyInfo.builder()
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
        @Schema(description = "기타사건 상세 응답")
        public record ConflictEtcCaseDetail(
                @Schema(description = "분쟁 일련번호") String conflictSeq,
                ConflictEtcCftCaseMng cftCaseMng,
                EtcAppBaseInfo appBaseInfo,
                ConflictAppTitleInfo appTitleInfo,
                ConflictAppGoodsInfo appGoodsInfo,
                ConflictEtcAppPartyInfo appPartyInfo,
                ConflictEtcCftLitigantInfo cftLitigantInfo,
                ConflictCftJudgmentInfo cftJudgmentInfo,
                ConflictCftManagerInfo cftManagerInfo,
                ConflictCftNoteInfo cftNoteInfo,
                ConflictFiles etcConflictFile
        ) {
            public static ConflictEtcCaseDetail from(ConflictMergeVO vo, List<PaperResponseVO> fileList) {
                if (vo == null) return null;
                return ConflictEtcCaseDetail.builder()
                        .conflictSeq(vo.getConflictSeq())
                        .cftCaseMng(ConflictEtcCftCaseMng.from(vo))
                        .appBaseInfo(EtcAppBaseInfo.from(vo))
                        .appTitleInfo(ConflictAppTitleInfo.from(vo))
                        .appGoodsInfo(ConflictAppGoodsInfo.from(vo))
                        .appPartyInfo(ConflictEtcAppPartyInfo.from(vo))
                        .cftLitigantInfo(ConflictEtcCftLitigantInfo.from(vo))
                        .cftJudgmentInfo(ConflictCftJudgmentInfo.from(vo))
                        .cftManagerInfo(ConflictCftManagerInfo.from(vo))
                        .cftNoteInfo(ConflictCftNoteInfo.from(vo))
                        .etcConflictFile(ConflictFiles.from(fileList))
                        .build();
            }
        }


//    @Builder
//    public record ConflictEtcListDetail(
//            String conflictSeq,
//            String caseNo,          // 사건번호
//            String domesticRegNo,   // 국내등록번호 (핵심)
//
//            String status,          // 상태
//            String clientName,      // 의뢰인 (injectData)
//            String receiptDate,     // 접수일 (injectData)
//            String dueLimitDate,    // 처리마감일 (injectData)
//            String ourRef,
//            String yourRef,
//            String caseTypeCode     // 사건구분
//    ) {}


    // ConflictResponse 내부에 추가
    @Builder
    @Schema(description = "기타사건 목록 응답")
    public record ConflictEtcList(
            List<ConflictEtcListDetail> etcList,
            int totalCount
    ) {
        public static ConflictEtcList from(List<ConflictEtcListDetail> list) {
            return ConflictEtcList.builder()
                    .etcList(list)
                    .totalCount(list == null ? 0 : list.size())
                    .build();
        }
    }

}