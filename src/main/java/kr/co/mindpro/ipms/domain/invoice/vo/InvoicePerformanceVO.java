package kr.co.mindpro.ipms.domain.invoice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * 청구서 테이블 매핑 객체
 * DB 테이블 화면과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : InvoiceMergeVO.java
 * @since	 : 2026. 01. 07.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class InvoicePerformanceVO extends BaseVO {

    @Schema(description = "실적 분배 일련번호", example = "PERF20260000001", format = "SEQ")
    private String performanceSeq;

    @Schema(description = "청구서 일련번호", example = "INV20260000001", format = "SEQ")
    private String invoiceSeq;

    @Schema(description = "사무소 일련번호", example = "OFFICE20260000001")
    private String officeSeq;

    @Schema(description = "실적 구분 코드", example = "PERF01")
    private String performanceCategoryCode;

    @Schema(description = "실적 구분 명칭", example = "영업실적")
    private String performanceCategoryName;

    @Schema(description = "부서 구분", example = "영업부")
    private String deptCategory;

    @Schema(description = "담당자 일련번호", example = "USR20260000001", format = "SEQ")
    private String staff;

    @Schema(description = "담당자 명칭", example = "홍길동")
    private String staffName;

    @Schema(description = "실적 내용/역할", example = "주담당자")
    private String performanceContent;

    @Schema(description = "실적 인정일 (YYYYMMDD)", example = "20260101")
    private String performancePerfDate;

    @Schema(description = "실적 금액", example = "500000.00")
    private BigDecimal performanceAmount;

    @Schema(description = "분배 비율 (%)", example = "50.00")
    private BigDecimal shareRatio;

    @Schema(description = "비고", example = "특이사항 없음")
    private String note;
}