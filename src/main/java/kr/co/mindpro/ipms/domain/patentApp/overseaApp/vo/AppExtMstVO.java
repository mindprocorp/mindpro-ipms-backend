package kr.co.mindpro.ipms.domain.patentApp.overseaApp.vo; // 패키지 경로는 프로젝트에 맞게 확인!

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author : seokho
 * @fileName : AppExtMstVO.java
 * @since : 2026. 1. 28.
 * @description : 해외 출원 확장 정보(지정국가, 명세서 등) VO (utb_app_ext_mst)
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppExtMstVO extends BaseVO {

    /* --- PK Fields --- */
    // 사무소 식별자
    private String officeSeq;

    // 해외출원 식별자
    private String appExtSeq;

    /* --- General Info --- */
    // 권리구분 / 권리
    private String rightCategoryCode;
    private String rightCategoryName;

    // 출원 종류
    private String appKindCode;
    private String appKindName;

    // 출원 상태
    private String appStateCode;
    private String appStateName;

    // 의뢰인 정보
    private String clientSeq;
    private String clientName;

    // 출원인 정보
    private String applicantSeq;
    private String applicantName;

    // 관리담당자
    private String adminMgrSeq;
    private String adminMgrName;

    // 사건담당자
    private String caseMgrSeq;
    private String caseMgrName;

    // 담당변리사
    private String attorneySeq;
    private String attorneyName;

    // ourRef
    private String assetNo;

    // 사건 번호 / 사건 코드
    private String caseCode;

    // 부서코드
    private String deptCode;

    /* --- name info (명칭 정보) --- */
    // 출원의 국문명
    private String appNameKo;

    // 출원의 영문명
    private String appNameEn;

    /* --- 물품류 --- */
    // 물품류
    private String productClass;

    /* --- Designated States (지정국가 정보) --- */
    // 개국(개별국) 지정 수
    private Integer individualCountryCnt;

    // 개국(개별국) 내용
    private String individualCountryContent;

    // PCT 지정 수
    private Integer pctCnt;

    // PCT 지정 내용
    private String pctContent;

    // EP 지정 수
    private Integer epCnt;

    // EP 지정 내용
    private String epContent;

    // 마드리드 지정 수
    private Integer madridCnt;

    // 마드리드 지정 내용
    private String madridContent;

    // 국제 디자인 지정 수
    private Integer internationalDesignCnt;

    // 국제 디자인 지정 내용
    private String internationalDesignContent;

    /* --- Specification & Drawing (명세서 및 도면) --- */
    // 등급
    private String gradeCode;
    private String gradeName;

    // 독립항 수
    private Integer independentClaimCnt;

    // 종속항 수
    private Integer dependentClaimCnt;

    // 명세서 페이지 수
    private Integer overseaSpecCnt;

    // 도면 수
    private Integer drawingCnt;

    // 국제 출원 번호
    private String globalAppNo;

    // 대표 파일 식별자
    private String representativeFileSeq;

    /* --- Abandon Info --- */
    // 포기 내용
    private String giveUpContent;



    /* --- 관계자 정보 --- */
    private String client;               // 의뢰인 식별키
    private String clientNm;              // 의뢰인

    private String applicant;            // 출원인 식별키
    private String applicantNm;           // 출원인

    private String inventor;            // 창작자 식별키
    private String inventorNm;        // 창작자

    private String regMgr;               // 등록권리자 식별키
    private String regMgrNm;              // 등록권리자?? (MergeVO: regMgrNm 매핑)

    private String appManager;          // 출원담당자 식별키
    private String appManagerNm;        // 출원담당자

    private String clientContact;        // 의뢰인담당자 식별키
    private String clientContactNm;       // 의뢰인담당자

    private String adminMgr;             // 관리담당자 식별키
    private String adminMgrNm;            // 관리담당자

    private String caseMgr;              // 사건담당자 식별키
    private String caseMgrNm;             // 사건담당자

    private String attorney;             // 담당변리사 식별키
    private String attorneyNm;            // 담당변리사


    /* --- 기일 정보 --- */
    private String receiptDate;
    private String appCompleteDate;
    private String globalAppDate;
    private String abandonDate;
}