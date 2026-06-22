package kr.co.mindpro.ipms.domain.bizinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

/**
 * 기일 테이블 매핑 객체
 * DB 테이블 Duedate_mst 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : mindpro
 * @fileName	 : DuedateVO.java
 * @since	 : 2026. 01. 07.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BizInfoVO extends BaseVO {
    @Schema(description = "사업자 정보 일련번호", example = "BIZ202600001")
    private String bizInfoSeq;

    @Schema(description = "사무소 일련번호", example = "OFFICE001")
    private String officeSeq;

    @Schema(description = "법인 코드", example = "CORP001")
    private String corpCode;

    @Schema(description = "사업장 상호", example = "(주)마인드프로")
    private String bizCorpName;

    @Schema(description = "사업자 등록번호", example = "123-45-67890")
    private String bizRegNo;

    @Schema(description = "종사업장번호", example = "123-45-67890")
    private String bizWorkplaceNo;

    @Schema(description = "대표자 성명", example = "홍길동")
    private String ceoName;

    @Schema(description = "사업장 주소", example = "서울특별시 강남구 ...")
    private String bizAddr;

    @Schema(description = "사업장 상세 주소", example = "101호")
    private String bizAddrDetail;

    @Schema(description = "우편번호", example = "06000")
    private String bizPostNo;

    @Schema(description = "전화번호", example = "02-123-4567")
    private String bizTelNo;

    @Schema(description = "팩스번호", example = "02-123-4568")
    private String bizFaxNo;

    @Schema(description = "업태", example = "서비스")
    private String bizType;

    @Schema(description = "종목", example = "소프트웨어 개발")
    private String bizKind;

    @Schema(description = "등록 감면 코드", example = "RED01")
    private String regDiscountCode;

    @Schema(description = "연차 감면 코드", example = "YRED01")
    private String yearDiscountCode;

    @Schema(description = "감면 종료일 (면제기간 까지)")
    private String discountClosingDate;

    @Schema(description = "감면 사류/사유")
    private String reductionReason;

    @Schema(description = "감면 발급일")
    private String reductionIssueDate;

    @Schema(description = "사업자담당자", example = "이지금")
    private String bizContactName;

    @Schema(description = "사업자부서", example = "경리부")
    private String bizDeptName;

    @Schema(description = "사업자이메일", example = "acc@mindpro.co.kr")
    private String bizEmail;


}