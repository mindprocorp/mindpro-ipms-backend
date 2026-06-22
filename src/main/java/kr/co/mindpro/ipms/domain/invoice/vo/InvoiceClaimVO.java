package kr.co.mindpro.ipms.domain.invoice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

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
public class InvoiceClaimVO extends BaseVO {

    @Schema(description = "청구내역 일련번호", example = "CLAIM20260000001", format = "SEQ")
    private String invoiceClaimSeq;

    @Schema(description = "청구서 일련번호", example = "INV20260000001", format = "SEQ")
    private String invoiceSeq;

    @Schema(description = "사무소 일련번호", example = "OFFICE20260000001")
    private String officeSeq;

    @Schema(description = "청구 종류", example = "청구내역")
    private String claimKind;

    @Schema(description = "비용 구분 코드", example = "COST01")
    private String costCategoryCode;

    @Schema(description = "비용 구분 명칭", example = "특허료")
    private String costCategoryName;

    @Schema(description = "청구 내역 내용", example = "출원 관납료")
    private String itemContent;

    @Schema(description = "수량 단위 코드", example = "UNIT01")
    private String unitCode;

    @Schema(description = "수량 단위 명칭", example = "건")
    private String unitName;

    @Schema(description = "단가", example = "100000.00")
    private BigDecimal unitPrice;

    @Schema(description = "수량", example = "1.00")
    private BigDecimal quantity;

    @Schema(description = "금액", example = "100000.00")
    private BigDecimal amount;

    @Schema(description = "부가세", example = "10000.00")
    private BigDecimal vatAmount;

    @Schema(description = "합계 금액", example = "110000.00")
    private BigDecimal totalAmount;

    @Schema(description = "비고", example = "기타 참고사항")
    private String note;
}