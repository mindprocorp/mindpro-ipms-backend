package kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author : seokho
 * @fileName : PatentAppMstVO.java
 * @since : 2026. 1. 8.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppMstVO extends BaseVO {

    /** 리스트 순번 */
    private int rowNum;

    /** 해외 기본 식별자(해외기본전용) */
    private String appExtSeq;

    /** 해외 맵핑 식별자 */
    private String extMappSeq;

    /** 사무소_식별자 */
    private String officeSeq;

    /** 출원_식별자 */
    private String appSeq;

    /** 특허/실용신안 식별자 */
    private String patentSeq;

    /** 디자인 식별자 */
    private String designSeq;

    /** 상표 식별자 */
    private String trademarkSeq;

    /** 해외_출원루트 */
    private String appRouteCode;
    private String appRouteName;

    /** 의뢰_식별자 */
    private String retainSeq;

    /** 출원_상태 */
    private String appStateCode;
    private String appStateName;

    /** 상태 */
    private String stateCode;
    private String stateName;

    /**
     * 관계자 정보
     * */
    private String clientSeq;               // 의뢰인 식별키
    private String clientName;              // 의뢰인

    private String applicantSeq;            // 출원인 식별키
    private String applicantName;           // 출원인

    private String inventorSeq;             // 발명자 식별키
    private String inventorName;            // 발명자

    private String regMgrSeq;               // 등록권리자 식별키
    private String regMgrName;              // 등록권리자?? (MergeVO: regMgrName 매핑)

    private String clientContactSeq;        // 출원인담당자 식별키
    private String clientContactName;       // 출원인담당자

    private String adminMgrSeq;             // 관리담당자 식별키
    private String adminMgrName;            // 관리담당자

    private String caseMgrSeq;              // 사건담당자 식별키
    private String caseMgrName;             // 사건담당자

    private String attorneySeq;             // 담당변리사 식별키
    private String attorneyName;            // 담당변리사

    private String foreignAgentSeq;         // 해외대리인 식별키
    private String foreignAgentName;        // 해외대리인

    /** 국가명(국문) */
    private String countryName;

    /** 국가_코드 */
    private String countryCode;

    /** 권리_구분 */
    private String rightCategoryCode;
    private String rightCategoryName;

    /** 상품_류 */
    private String productClass;

    /** 출원_한글_이름 */
    private String appNameKo;

    private String appNameEn;

    private String outsourcingCorpName;

    private String outsourcingYn;

    /** 포기 내용 */
    private String giveUpContent;

    /** 출원_구분 */
    private String appCategoryCode;
    private String appCategoryName;

    private String appName;

    private String gradeCode;
    private String gradeName;

    private String independentClaim;

    private String dependentClaim;

    private String drawingPaperCount;

    private Integer ultiDependentClaimCount;

    private String appNo;

    /** 원출원번호 */
    private String originalAppNo;

    /** 원등록번호 */
    private String originalRegNo;

    private String doubleAppNo;

    private String globalAppNo;

    private String globalRegNo;

    /** 등록 번호 */
    private String regNo;

    /** 출원 공개 번호 */
    private String openNo;

    private String productClassAppNo;

    private String reAppNo;

    /** 출원 공고번호 */
    private String publicNo;

    private String regPublicNo;

    private String madridNo;

    private String ipcCategoryCode;

    private String externalAppApproach;

    /** 연차관리 여부 */
    private String yearCntManagementYn;

    private String externalAppYn;

    private String trademarkResearchYn;

    /** 갱신관리여부 */
    private String renewalManagementYn;

    /** 갱신등록마감(해외 개국 상표 전용) */
    private String renewalDeadline;

    private String interiorPreferenceAssertYn;

    private String mandatePaperSubmitYn;

    private String note;

    /** 구분 (내국/외국/해외) */
    private String appClassificationCode;
    private String appClassificationName;

    /** 출원 종류 (신규출원/가출원 등) */
    private String appKindCode;
    private String appKindName;

    /** 출원 언어 (ko/en) */
    private String appLanguageCode;
    private String appLanguageName;

    /** 제안명칭 */
    private String proposalName;

    /** 해외 출원 시점 (동시/추후) */
    private String foreignAppTimingCode;
    private String foreignAppTimingName;

    /** 최초 출원 번호 */
    private String firstAppNo;

    /** 접근코드 */
    private String accessCode;

    /** 연차관리 - 차수 */
    private String annuityYear;

    /** 연차관리 - 감면율 */
    private String annuityReducRateCode;
    private String annuityReducRateName;

    /** (임시) 명세서 구성요소 - 명세서 */
    private String specPage;

    /** (임시) 명세서 구성요소 - 도수 */
    private String figureCount;

    /** 등록 감면율 */
    private String regReductionRateCode;
    private String regReductionRateName;

    /** ourRef */
    private String assetNo;

    /** yourRef */
    private String agentRef;

    /** 부서_코드 */
    private String deptCode;

    /** 차기납부차수 */
    private String nextPaymentInstallment;

    /** 상표갱신등록료 */
    private String trademarkRenewalFee;

    /** 상표갱신과태료 */
    private String renewalLateFee;

    /** 기타_표기_명칭 */
    private String etcTitle;

    /** 특/실 join 정보 */
    // 메인도면파일
    private String mainDrawingFile;

    /** 디자인 join 정보 */
    // 입체도 파일
    private String multiViewDrawingFile;

    /** 요약 내용 */
    private String summary;

    /** 청구 범위 */
    private String claimScope;

    /** 디자인 설명 */
    private String designDescription;

    /** 디자인 요약 */
    private String designSummary;

    // 모등록 번호
    private String parentRegAppNo;

    // 부분 디자인 여부
    private String isPartialDesign;

    // 다의장 (복수 디자인 수)
    private int multiDesign;

    /** 상표 join 정보 */
    // 상표 이미지 파일
    private String trademarkImageFile;

    /** 국내 등록번호 */
    private String domesticRegNo;

    /** 분류출원번호 */
    private String goodsAppNo;



    /** 해외 전용 */

    /** 해외_명세서 */
    private String overseaSpecPage;

    /** 해외출원 YN */
    private String isOversea;

    /** 가출원번호 */
    private String provisionalAppNo;

    /** 공지예외적용 (개국 > 디자인) */
    private String noticeExceptionApplyCode;
    private String noticeExceptionApplyName;



    /** 해외 개국 이외 항목 (pct, ep, 마드리드, 국제디자인) */

    /** 20개월 마감 정보 (pct) */
    // 20개월 완료 여부
    private String complete20Yn;
    // 20개월 지정국가
    private String app20Country;
    /** 30개월 마감 정보 (pct) */
    // 30개월 완료 여부
    private String complete30Yn;
    // 30개월 지정국가
    private String app30Country;

    /** 지정국가 (KR 지정 여부 - pct) */
    private String krDesignationYn;

    /** 국제조사 결과 */
    private String searchResult;

    /** 국제공개번호 */
    private String intlPubNo;

    /** 포기취하내용 */
    private String deemedWithdrawalContent;

    /** 지정국가 (개국, pct, ep, 마드리드, 국제디자인 포함.) */
    private String designated;

    /** 등록국가 (개국, pct, ep, 마드리드, 국제디자인 포함.) */
    private String registeredStates;

    /** 사후 지정국가 */
    private String subsequent;

    /** 분할출원번호 */
    private String divAppNo;

    /** 서치 결과 (ep 전용) */
    private String epSearchResult;

    /** WIPO 참조번호 */
    private String wipoRefNo;

    /** 출원 공개 선택 */
    private String publicYn;

    /** 연기월수 */
    private String defermentMonthCount;

    /** 출원공개일자 */
    private String pubDate;

    /** 출원공개번호 */
    private String pubNo;

    /** 특허청참조번호 */
    private String authorityRefNo;

    /** 갱신차수 */
    private String paymentInstallment;

}
