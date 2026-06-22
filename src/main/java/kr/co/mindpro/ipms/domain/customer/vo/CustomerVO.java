package kr.co.mindpro.ipms.domain.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 고객 테이블 매핑 객체
 * DB 테이블 Customer 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : CustomerVO.java
 * @since	 : 2026. 01. 07.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerVO extends BaseVO {
    @Schema(description = "고객 일련번호")
    private String customerSeq;      // 고객정보 일련번호

    @Schema(description = "사무소 일련번호")
    private String officeSeq;            // 사무소 일련번호

    // 구분 코드류
    @Schema(description = "고객구분 코드")
    private String clientCategoryCode;   // 고객구분

    @Schema(description = "출원인구분 코드")
    private String applicantCategoryCode;// 출원인구분

    @Schema(description = "기업구분 코드")
    private String corpCategoryCode;     // 기업구분

    @Schema(description = "변리사구분 코드")
    private String attorneyCategoryCode; // 변리사구분

    // 성명 및 명칭
    @Schema(description = "고객한글명")
    private String clientNameKo;         // 고객한글명

    @Schema(description = "고객영문명")
    private String clientNameEn;         // 고객영문명

    @Schema(description = "고객한자명")
    private String clientNameCh;         // 고객한자명

    @Schema(description = "고객일문명")
    private String clientNameJp;         // 고객일문명

    @Schema(description = "회사명")
    private String companyName;          // 회사명

    @Schema(description = "부서명")
    private String deptName;             // 부서명

    @Schema(description = "직책")
    private String customerPosition;     // 직책

    // 주소 및 연락처 (출원/연락/기타/해외)
    @Schema(description = "출원인 우편번호")
    private String appZipCode;

    @Schema(description = "출원인 주소")
    private String appAddress;

    @Schema(description = "출원인 전화번호")
    private String appTel;

    @Schema(description = "출원인 팩스번호")
    private String appFax;

    @Schema(description = "담당자 우편번호")
    private String contactZipCode;

    @Schema(description = "담당자 주소")
    private String contactAddress;

    @Schema(description = "담당자 성명")
    private String contactPerson;

    @Schema(description = "담당자 전화번호")
    private String contactTel;

    @Schema(description = "담당자 팩스번호")
    private String contactFax;

    @Schema(description = "기타 우편번호")
    private String etcZipCode;

    @Schema(description = "기타 주소")
    private String etcAddress;

    @Schema(description = "기타 전화번호")
    private String etcTel;

    @Schema(description = "기타 팩스번호")
    private String etcFax;

    @Schema(description = "해외 우편번호")
    private String overseaZipCode;

    @Schema(description = "해외 주소")
    private String overseaAddress;

    @Schema(description = "해외 전화번호")
    private String overseaTel;

    @Schema(description = "해외 팩스번호")
    private String overseaFax;

    // 국가 및 등록번호
    @Schema(description = "국가코드")
    private String countryCode;          // 국가코드

    @Schema(description = "주민등록번호")
    private String residentRegNo;        // 주민등록번호

    @Schema(description = "법인등록번호")
    private String corpRegNo;            // 법인등록번호

    @Schema(description = "특허고객번호")
    private String kipoClientNo;         // 특허고객번호

    @Schema(description = "관리자명")
    private String managerName;          // 관리자명

    @Schema(description = "포괄위임번호")
    private String generalMandateNo;     // 포괄위임번호

    // 기타 정보
    @Schema(description = "휴대폰번호")
    private String mobile;

    @Schema(description = "홈페이지")
    private String homepage;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "고객등록일 (YYYYMMDD)")
    private String registrationDate;     // 고객등록일 (YYYYMMDD)

    @Schema(description = "비고")
    private String note;

    @Schema(description = "사업자정보 일련번호")
    private String bizInfoSeq;           // 사업자정보 일련번호 (연동용 추가)

    @Schema(description = "사업자번호")
    private String bizRegNo;      // 사업자번호

    @Schema(description = "상호명")
    private String bizCorpName;   // 상호명

    @Schema(description = "대표자명")
    private String ceoName;       // 대표자명

    @Schema(description = "사업장주소")
    private String bizAddr;       // 사업장주소

    @Schema(description = "업태")
    private String bizType;       // 업태

    @Schema(description = "종목")
    private String bizKind;       // 종목

    @Schema(description = "종사업장번호")
    private String bizWorkplaceNo;       // 종사업장번호

    @Schema(description = "감면비율")
    private String reductionRate;        // 감면비율

    @Schema(description = "감면사유")
    private String reductionReason;      // 감면사유

    @Schema(description = "감면 등록 코드 (대상%)")
    private String regDiscountCode;

    @Schema(description = "감면 발급일")
    private String reductionIssueDate;

    @Schema(description = "감면 종료일 (면제기간 까지)")
    private String discountClosingDate;

    @Schema(description = "정렬순서")
    private int orderNo;
}