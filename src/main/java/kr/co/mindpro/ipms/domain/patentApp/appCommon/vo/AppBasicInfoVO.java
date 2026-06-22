package kr.co.mindpro.ipms.domain.patentApp.appCommon.vo;

/**
 * @author : seokho
 * @fileName : AppBaseCaseVO.java
 * @since : 2026. 1. 21.
 */
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author : seokho
 * @description : MyBatis 매핑이 용이한 평면 구조의 VO
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "출원기본 통합 정보 VO (조회용)")
public class AppBasicInfoVO {

    // key 정보
    @Schema(description = "사무소식별자")
    private String officeSeq;

    @Schema(description = "출원식별자")
    private String appSeq;

    /* --- 출원 사건관리 (Case Management) --- */
    // category
    @Schema(description = "구분", example = "A01")
    private String appClassification;

    // rightType
    @Schema(description = "권리", example = "C01")
    private String rightCategory;

    // appType
    @Schema(description = "출원종류", example = "B01")
    private String appKind;

    // appCategory
    @Schema(description = "출원구분", example = "F01")
    private String appCategory;

    @Schema(description = "국가코드")
    private String countryCode;

    @Schema(description = "국가 국문명")
    private String ctryKoNm;

    @Schema(description = "발명신고일", example = "20260109")
    private String inventionReportDate;

    @Schema(description = "접수일", example = "20260109")
    private String receiptDate;

    // ourRef
    @Schema(description = "OurRef", example = "OUR123456")
    private String assetNo;

    // yourRef
    @Schema(description = "YourRef", example = "YOUR123456")
    private String agentRef;

    // clientRef
    @Schema(description = "출원인관리번호(ClientRef)", example = "APPMNG123456")
    private String retainSeq;

    @Schema(description = "초안마감일", example = "20260109")
    private String draftDeadline;

    @Schema(description = "초안발송일", example = "20260109")
    private String draftSendDate;

    /* --- 출원 기본정보 (Base Information) --- */
    @Schema(description = "출원지시일", example = "20260109")
    private String appOrderDate; 

    @Schema(description = "출원마감일", example = "20260109")
    private String appDeadline; 

    @Schema(description = "출원일", example = "20260109")
    private String appDate; 

    // appNo
    @Schema(description = "출원번호", example = "10-2026-1234567")
    private String appNo;

    // accessCode
    @Schema(description = "접근코드", example = "test")
    private String accessCode;

    // appLanguage
    @Schema(description = "출원언어", example = "ko")
    private String appLanguage;

    @Schema(description = "번역문마감일", example = "20260109")
    private String transDeadline;

    @Schema(description = "번역문제출일", example = "20260109")
    private String transSubmitDate;

    /** 담당 정보 */
    @Schema(description = "부서", example = "test")
    private String deptName;

    @Schema(description = "관리담당자_seq", example = "USERIF20260000002")
    private String adminMgrSeq;

    @Schema(description = "관리담당자_이름", example = "김법인")
    private String adminMgrName;

    @Schema(description = "사건담당자_seq", example = "USERIF20260000002")
    private String caseMgrSeq;

    @Schema(description = "사건담당자_이름", example = "김법인")
    private String caseMgrName;

    @Schema(description = "담당변리사_seq", example = "USERIF20260000002")
    private String attorneySeq;

    @Schema(description = "담당변리사_이름", example = "김법인")
    private String attorneyName;

    /** 당사자 정보 */
    @Schema(description = "의뢰인_seq", example = "USERIF20260000002")
    private String clientSeq;

    @Schema(description = "의뢰인_이름", example = "USERIF20260000002")
    private String clientName;

    @Schema(description = "의뢰인담당자_seq", example = "USERIF20260000002")
    private String clientContactSeq;

    @Schema(description = "의뢰인_이름", example = "USERIF20260000002")
    private String clientContactName;

    @Schema(description = "출원인_seq", example = "USERIF20260000002")
    private String applicantSeq;

    @Schema(description = "출원인_이름", example = "USERIF20260000002")
    private String applicantName;

    @Schema(description = "발명자_seq", example = "USERIF20260000002")
    private String inventorSeq;

    @Schema(description = "발명자_이름", example = "USERIF20260000002")
    private String inventorName;

    @Schema(description = "등록권리자_seq", example = "USERIF20260000002")
    private String regMgrSeq;

    @Schema(description = "등록권리자_이름", example = "USERIF20260000002")
    private String regMgrName;

    /** 명칭정보 */
    @Schema(description = "제안", example = "testVal")
    private String proposal;

    @Schema(description = "국문 명칭", example = "testVal")
    private String titleKo;

    @Schema(description = "영문 명칭", example = "testVal")
    private String titleEn;

    @Schema(description = "기타_표기_명칭", example = "testVal")
    private String etcTitle;

    @Schema(description = "물품류(상품류)")
    private String goodsClass;
}

