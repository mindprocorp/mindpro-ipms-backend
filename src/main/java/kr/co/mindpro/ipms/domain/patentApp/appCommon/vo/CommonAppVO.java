package kr.co.mindpro.ipms.domain.patentApp.appCommon.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author : seokho
 * @fileName : CommonAppVO.java
 * @since : 2026. 04. 10.
 * @description : 국내/해외/해외기초 통합 출원 마스터 VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommonAppVO extends BaseVO {

    // ==========================================
    // [0] 기본 식별자 및 상태
    // ==========================================
    @Schema(description = "해외 기본 식별자(해외기본전용)")
    private String appExtSeq;

    @Schema(description = "해외 맵핑 식별자")
    private String extMappSeq;

    @Schema(description = "사무소 식별자")
    private String officeSeq;

    @Schema(description = "출원 식별자(PK)")
    private String appSeq;

    @Schema(description = "특허/실용신안 식별자")
    private String patentSeq;

    @Schema(description = "디자인 식별자")
    private String designSeq;

    @Schema(description = "상표 식별자")
    private String trademarkSeq;

    @Schema(description = "출원 히스토리 식별자")
    private String appHistorySeq;


    // ==========================================
    // [1] 사건관리 및 루트
    // ==========================================
    @Schema(description = "출원루트 코드")
    private String appRouteCode;

    @Schema(description = "출원루트명")
    private String appRouteName;

    @Schema(description = "출원인관리번호")
    private String clientRef;

    @Schema(description = "출원 상태 코드")
    private String statusCode;

    @Schema(description = "출원 상태명")
    private String statusName;

    @Schema(description = "상태 코드")
    private String stateCode;

    @Schema(description = "상태명")
    private String stateName;

    @Schema(description = "구분 코드")
    private String categoryCode;        // appClassificationCode (컬럼명)

    @Schema(description = "구분명")
    private String categoryName;

    @Schema(description = "권리 구분 코드")
    private String rightTypeCode;

    @Schema(description = "권리 구분명")
    private String rightTypeName;

    @Schema(description = "출원 구분 코드")
    private String appCategoryCode;

    @Schema(description = "출원 구분명")
    private String appCategoryName;

    @Schema(description = "출원 종류 코드")
    private String appTypeCode;

    @Schema(description = "출원 종류명")
    private String appTypeName;

    @Schema(description = "출원 언어 코드")
    private String appLanguageCode;

    @Schema(description = "출원 언어명")
    private String appLanguageName;


    // ==========================================
    // [2] 참조 번호
    // ==========================================
    @Schema(description = "OurRef")
    private String ourRef;

    @Schema(description = "YourRef")
    private String yourRef;

    @Schema(description = "WIPO 참조번호")
    private String wipoRefNo;

    @Schema(description = "특허청 참조번호")
    private String authorityRefNo;

    @Schema(description = "접근코드")
    private String accessCode;

    @Schema(description = "사건 번호 / 사건 코드")
    private String caseNo;


    // ==========================================
    // [3] 명칭 및 물품
    // ==========================================
    @Schema(description = "국문 명칭")
    private String titleKo;

    @Schema(description = "영문 명칭")
    private String titleEn;

    @Schema(description = "제안명칭")
    private String proposal;

    @Schema(description = "기타 표기 명칭")
    private String etcTitle;

    @Schema(description = "물품류")
    private String goodsClass;

    @Schema(description = "복수디자인여부/내용")
    private String multiDesign;


    // ==========================================
    // [4] 당사자 정보 (ID/Sequence 필드)
    // ==========================================
    @Schema(description = "의뢰인 식별키")
    private String client;

    @Schema(description = "출원인 식별키")
    private String applicant;

    @Schema(description = "출원인담당자 식별키")
    private String applicantContact;

    @Schema(description = "의뢰인담당자 식별키")
    private String clientContact;

    @Schema(description = "발명자/창작자 식별키")
    private String inventor;

    @Schema(description = "등록권리자 식별키")
    private String regMgr;

    @Schema(description = "출원담당자 식별키")
    private String appManager;

    @Schema(description = "관리담당자 식별키")
    private String adminMgr;

    @Schema(description = "사건담당자 식별키")
    private String caseMgr;

    @Schema(description = "담당변리사 식별키")
    private String attorney;

    @Schema(description = "해외대리인 식별키")
    private String foreignAgent;

    @Schema(description = "부서")
    private String deptName;


    // ==========================================
    // [4-1] 당사자 정보 (성명 Nm 표준 필드)
    // ==========================================
    @Schema(description = "의뢰인명")
    private String clientNm;

    @Schema(description = "출원인명")
    private String applicantNm;

    @Schema(description = "출원인담당자명")
    private String applicantContactNm;

    @Schema(description = "의뢰인담당자명")
    private String clientContactNm;

    @Schema(description = "발명자/창작자명")
    private String inventorNm;

    @Schema(description = "등록권리자명")
    private String regMgrNm;

    @Schema(description = "출원담당자명")
    private String appManagerNm;

    @Schema(description = "관리담당자명")
    private String adminMgrNm;

    @Schema(description = "사건담당자명")
    private String caseMgrNm;

    @Schema(description = "담당변리사명")
    private String attorneyNm;

    @Schema(description = "해외대리인명")
    private String foreignAgentNm;


    // ==========================================
    // [5] 국가 정보
    // ==========================================
    @Schema(description = "국가명(국문)")
    private String countryName;

    @Schema(description = "국가 코드")
    private String countryCode;

    @Schema(description = "출원 국가명")
    private String appCountry;      // ?????

    @Schema(description = "출원 국가코드")
    private String appCountryCode;  // ?????


    // ==========================================
    // [6] 명세서 및 도면
    // ==========================================
    @Schema(description = "등급 코드")
    private String gradeCode;

    @Schema(description = "등급명")
    private String gradeName;

    @Schema(description = "독립항 수")
    private String independentClaims;

    @Schema(description = "종속항 수")
    private String dependentClaims;

    @Schema(description = "국내 명세서 페이지")
    private String specPage;

    @Schema(description = "해외 명세서 페이지")
    private String overseaSpecPage;

    @Schema(description = "도면수")
    private String drawingCount;

    @Schema(description = "도수")
    private String figureCount;

    @Schema(description = "최종항수")
    private Integer finalClaimsCount;

    @Schema(description = "대표도면/이미지 파일")
    private String mainDrawingFile;

    @Schema(description = "대표 파일 식별자 (AppExtMst 전용)")
    private String representativeFileSeq;


    // ==========================================
    // [7] 텍스트 상세
    // ==========================================
    @Schema(description = "요약")
    private String summary;

    @Schema(description = "청구범위")
    private String claimScope;

    @Schema(description = "디자인설명")
    private String designDescription;

    @Schema(description = "디자인요약")
    private String designSummary;

    @Schema(description = "사시도")
    private String multiViewDrawingFile;

    @Schema(description = "상표이미지_파일")
    private String trademarkImageFile;

    @Schema(description = "비고")
    private String note;


    // ==========================================
    // [8] 번호 정보
    // ==========================================
    @Schema(description = "출원번호")
    private String appNo;

    @Schema(description = "출원공개일자")
    private String pubDate;

    @Schema(description = "출원공개번호")
    private String pubNo;

    @Schema(description = "출원공고_일자")
    private String announcementDate;

    @Schema(description = "출원공고_번호")
    private String announcementNo;

    @Schema(description = "등록번호")
    private String regNo;

    @Schema(description = "등록공고_일자")
    private String regAnnounceDate;

    @Schema(description = "등록공고_번호")
    private String regAnnounceNo;

    @Schema(description = "마드리드번호")
    private String madridNo;


    // ==========================================
    // [9] 전략/패밀리 번호
    // ==========================================
    @Schema(description = "분류출원번호")
    private String classificAppNo;

    @Schema(description = "최초출원번호")
    private String firstAppNo;

    @Schema(description = "가출원번호")
    private String provisionalAppNo;

    @Schema(description = "분할출원번호")
    private String divAppNo;

    @Schema(description = "원출원번호")
    private String originalAppNo;

    @Schema(description = "원등록번호")
    private String originalRegNo;

    @Schema(description = "재출원번호")
    private String reAppNo;

    @Schema(description = "이중출원번호")
    private String dualAppNo;

    @Schema(description = "국제출원번호")
    private String globalAppNo;

    @Schema(description = "국제등록번호")
    private String globalRegNo;

    @Schema(description = "국제공개번호")
    private String intlPubNo;

    @Schema(description = "국내등록번호")
    private String domesticRegNo;

    @Schema(description = "마드리드번호")
    private String madridAppNo;

    @Schema(description = "상품 류 출원 번호")
    private String productClassAppNo;


    // ==========================================
    // [10] 기일 정보 (Case Management)
    // ==========================================
    @Schema(description = "출원지시일")
    private String appOrderDate;

    @Schema(description = "출원마감일")
    private String appDeadline;

    @Schema(description = "출원일")
    private String appDate;

    @Schema(description = "접수일")
    private String receiptDate;

    @Schema(description = "출원 완료일")
    private String appCompleteDate;

    @Schema(description = "발명신고일")
    private String inventionReportDate;

    @Schema(description = "초안마감일")
    private String draftDeadline;

    @Schema(description = "초안발송일")
    private String draftSendDate;

    @Schema(description = "분할출원마감일")
    private String divDeadline;

    @Schema(description = "분할출원일")
    private String divAppDate;

    @Schema(description = "자동보호 결정일")
    private String autoProtectionDate;

    @Schema(description = "특허청제출일")
    private String authoritySubmissionDate;

    @Schema(description = "헤이그발송일")
    private String hagueDeliveryDate;

    @Schema(description = "OA 발송일")
    private String oaDeliveryDate;

    @Schema(description = "공지 예외 적용 코드")
    private String noticeExceptionApplyCode;

    @Schema(description = "공지 예외 적용명")
    private String noticeExceptionApplyName;


    // ==========================================
    // [11] 기일 정보 (Strategy & Relations)
    // ==========================================
    @Schema(description = "가출원일")
    private String provisionalAppDate;

    @Schema(description = "최초출원일")
    private String firstAppDate;

    @Schema(description = "원출원일")
    private String originalAppDate;

    @Schema(description = "원등록일")
    private String originalRegDate;

    @Schema(description = "재출원일")
    private String reAppDate;

    @Schema(description = "이중출원일")
    private String dualAppDate;

    @Schema(description = "국제출원일")
    private String globalAppDate;

    @Schema(description = "모등록일")
    private String parentRegAppDate;

    @Schema(description = "모등록번호")
    private String parentRegAppNo;

    @Schema(description = "마드리드출원일")
    private String madridAppDate;


    // ==========================================
    // [12] 기일 정보 (PCT/EP)
    // ==========================================
    @Schema(description = "20개월 진입마감일")
    private String npe20Deadline;

    @Schema(description = "20개월 진입완료일")
    private String entry20CompleteDate;

    @Schema(description = "30개월 진입마감일")
    private String npe30Deadline;

    @Schema(description = "30개월 진입완료일")
    private String entry30CompleteDate;

    @Schema(description = "수수료 마감일")
    private String filingFeeDeadline;

    @Schema(description = "수수료 납부일")
    private String filingFeePayDate;

    @Schema(description = "국제조사 접수일")
    private String isaReceiptDate;

    @Schema(description = "국제조사 보고일")
    private String isrReportDate;

    @Schema(description = "예비심사 마감일")
    private String ipeDeadline;

    @Schema(description = "예비심사 청구일")
    private String ipeRequestDate;

    @Schema(description = "예비심사 보고일")
    private String ipeReportDate;

    @Schema(description = "국제공개 접수일")
    private String intlReceiptDate;

    @Schema(description = "국제공개일")
    private String intlPubDate;


    // ==========================================
    // [13] 기일 정보 (Examination & Management)
    // ==========================================
    @Schema(description = "심사청구마감일")
    private String examRequestDeadline;

    @Schema(description = "심사청구지시일")
    private String examRequestOrderDate;

    @Schema(description = "심사청구일")
    private String examRequestDate;

    @Schema(description = "우선심사청구일")
    private String priorExamReqDate;

    @Schema(description = "우선심사결정일")
    private String priorExamDecDate;

    @Schema(description = "공개신청일")
    private String earlyPubRequestDate;

    @Schema(description = "공고결정일")
    private String announcementDecisionDate;

    @Schema(description = "보정통지일")
    private String amendNoticeDate;

    @Schema(description = "보정마감일")
    private String amendDeadline;

    @Schema(description = "보정제출일")
    private String amendSubmitDate;

    @Schema(description = "청구보정일")
    private String claimAmendDate;

    @Schema(description = "청구범위통지일")
    private String claimsNoticeDate;

    @Schema(description = "청구범위마감일")
    private String claimsDeadline;

    @Schema(description = "청구범위제출일")
    private String claimsSubmitDate;


    // ==========================================
    // [14] 기일 정보 (Abandonment/Withdrawal)
    // ==========================================
    @Schema(description = "포기지시일")
    private String abandonOrderDate;

    @Schema(description = "포기접수일")
    private String abandonReceiptDate;

    @Schema(description = "포기일자")
    private String abandonDate;

    @Schema(description = "포기내용")
    private String abandonNote;

    @Schema(description = "취하간주 접수일")
    private String deemedWithdrawalReceiptDate;

    @Schema(description = "취하간주 일자")
    private String deemedWithdrawalDate;

    @Schema(description = "취하간주 내용")
    private String deemedWithdrawalContent;


    // ==========================================
    // [15] 기일 정보 (Registration & Maintenance)
    // ==========================================
    @Schema(description = "등록결정일")
    private String regDecisionDate;

    @Schema(description = "등록접수일")
    private String regReceiptDate;

    @Schema(description = "등록정상마감일")
    private String regNormalDeadline;

    @Schema(description = "등록과태마감일")
    private String regGraceDeadline;

    @Schema(description = "등록지시일")
    private String regOrderDate;

    @Schema(description = "등록납부일")
    private String regPaymentDate;

    @Schema(description = "등록일")
    private String regDate;

    @Schema(description = "국내등록일")
    private String domesticRegDate;

    @Schema(description = "보호시작일")
    private String protectionStartDate;

    @Schema(description = "EP공고일")
    private String epAnnouncementDate;

    @Schema(description = "EP등록결정일")
    private String epRegDecisionDate;

    @Schema(description = "EP등록정상마감일")
    private String epRegNormalDeadline;

    @Schema(description = "EP등록과태마감일")
    private String epRegGraceDeadline;

    @Schema(description = "EP등록지시일")
    private String epRegOrderDate;

    @Schema(description = "EP등록납부일")
    private String epRegPaymentDate;

    @Schema(description = "서치(EP)접수일")
    private String searchReceiptDate;

    @Schema(description = "서치(EP)보고일")
    private String searchReportDate;

    @Schema(description = "갱신마감일")
    private String renewalDeadline;

    @Schema(description = "연차정상마감일")
    private String standardDeadline;

    @Schema(description = "연차과태마감일")
    private String penaltyDeadline;

    @Schema(description = "연차회복마감일")
    private String recoveryDeadline;

    @Schema(description = "국내우선권주장마감일")
    private String domesticPriorDeadline;

    @Schema(description = "국내우선권주장일")
    private String domesticPriorDate;

    @Schema(description = "번역마감일")
    private String transDeadline;

    @Schema(description = "번역제출일")
    private String transSubmitDate;

    @Schema(description = "해외6월마감일")
    private String foreign6mDeadline;

    @Schema(description = "해외1년마감일")
    private String foreign1yDeadline;

    @Schema(description = "해외출원일")
    private String foreignAppDate;

    @Schema(description = "연차료지시일")
    private String annuityOrderDate;


    // ==========================================
    // [16] 행정 및 관리
    // ==========================================
    @Schema(description = "IPC 분류")
    private String ipcClassification;

    @Schema(description = "위임업체명")
    private String annuityAgency;

    @Schema(description = "위임업체여부")
    private String outsourcingYn;

    @Schema(description = "공지예외적용코드")
    private String noticeExceptionApplyCodeMgmt;

    @Schema(description = "연차관리여부")
    private String isAnnuityManaged;

    @Schema(description = "갱신관리여부")
    private String isRenewalManaged;

    @Schema(description = "위임장제출여부")
    private String isPoaSubmitted;

    @Schema(description = "부분디자인여부")
    private String isPartialDesign;

    @Schema(description = "상표조사여부")
    private String isTrademarkResearch;

    @Schema(description = "해외출원여부")
    private String isForeignApp;

    @Schema(description = "국내우선권주장여부")
    private String hasDomesticPriority;

    @Schema(description = "특허청지연일")
    private Integer kipoDelayDays;

    @Schema(description = "권리존속기간")
    private String rightPeriod;


    // ==========================================
    // [17] 연차/갱신 정보
    // ==========================================
    @Schema(description = "연차관리차수")
    private String annuityYear;

    @Schema(description = "연차감면율코드")
    private String annuityReducRateCode;

    @Schema(description = "연차감면율명")
    private String annuityReducRateName;

    @Schema(description = "등록감면율코드")
    private String regReductionRateCode;

    @Schema(description = "등록감면율명")
    private String regReductionRateName;

    @Schema(description = "차기납부차수")
    private String nextPaymentInstallment;

    @Schema(description = "갱신차수")
    private String paymentInstallment;

    @Schema(description = "상표갱신료")
    private String trademarkRenewalFee;

    @Schema(description = "갱신과태료")
    private String renewalLateFee;


    // ==========================================
    // [18] 해외 및 해외기초 전용 정보
    // ==========================================
    @Schema(description = "KR지정여부")
    private String krDesignationYn;

    @Schema(description = "20개월진입완료여부")
    private String complete20Yn;

    @Schema(description = "20개월진입국가")
    private String app20Country;

    @Schema(description = "30개월진입완료여부")
    private String complete30Yn;

    @Schema(description = "30개월진입국가")
    private String app30Country;

    @Schema(description = "국제조사결과")
    private String searchResult;

    @Schema(description = "EP서치결과")
    private String epSearchResult;

    @Schema(description = "지정국가")
    private String designated;

    @Schema(description = "지정국가 - 개별국")
    private String designatedIndividual;

    @Schema(description = "지정국가 - PCT")
    private String designatedPct;

    @Schema(description = "지정국가 - EP")
    private String designatedEp;

    @Schema(description = "지정국가 - 마드리드")
    private String designatedMadrid;

    @Schema(description = "지정국가 - 국제디자인")
    private String designatedIntlDesign;

    @Schema(description = "등록국가")
    private String registeredStates;

    @Schema(description = "사후지정국가")
    private String subsequent;

    @Schema(description = "공개여부")
    private String publicYn;

    @Schema(description = "연기월수")
    private String defermentMonthCount;

    @Schema(description = "해외 출원 시점 코드 (동시/추후)")
    private String foreignAppTimingCode;

    @Schema(description = "해외 출원 시점명")
    private String foreignAppTimingName;

    @Schema(description = "제안명칭 (상세)")
    private String proposalName;

    @Schema(description = "출원명 (appName)")
    private String appName;

    @Schema(description = "개국(개별국) 지정 수")
    private Integer individualCountryCnt;

//    @Schema(description = "개국(개별국) 내용")
//    private String individualCountryContent;

    @Schema(description = "PCT 지정 수")
    private Integer pctCnt;

//    @Schema(description = "PCT 지정 내용")
//    private String pctContent;

    @Schema(description = "EP 지정 수")
    private Integer epCnt;

//    @Schema(description = "EP 지정 내용")
//    private String epContent;

    @Schema(description = "마드리드 지정 수")
    private Integer madridCnt;

//    @Schema(description = "마드리드 지정 내용")
//    private String madridContent;

    @Schema(description = "국제 디자인 지정 수")
    private Integer internationalDesignCnt;

//    @Schema(description = "국제 디자인 지정 내용")
//    private String internationalDesignContent;


    // ==========================================
    // [19] 기타 필드 (Misc)
    // ==========================================
    @Schema(description = "해외 출원 접근 방식")
    private String externalAppApproach;

    @Schema(description = "국내 우선권 주장 여부")
    private String interiorPreferenceAssertYn;

    @Schema(description = "상품류 출원번호")
    private String goodsAppNo;

    @Schema(description = "우선권 날짜")
    private String priorityDate;

    @Schema(description = "권리 스냅샷")
    private String rightSnapshot;
}