package kr.co.mindpro.ipms.domain.invoice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 청구서 테이블 매핑 객체
 * DB 테이블 InvoiceMstVO 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : InvoiceMstVO.java
 * @since	 : 2026. 01. 07.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class InvoiceMstVO extends BaseVO {

    @Schema(description = "사무소 일련번호", example = "OFFICE20260000001")
    private String officeSeq;

    @Schema(description = "청구서 일련번호", example = "INV20260000001", format = "SEQ")
    private String invoiceSeq;

    @Schema(description = "사건 일련번호", example = "APP20260000001", format = "SEQ")
    private String appSeq;

    @Schema(description = "고객 일련번호", example = "CUST20260000001", format = "SEQ")
    private String customerSeq;

    @Schema(description = "사업자정보 일련번호", example = "BIZ20260000001", format = "SEQ")
    private String bizInfoSeq;

    @Schema(description = "청구서 번호", example = "INV-2026-0001")
    private String invoiceNo;

    @Schema(description = "청구 구분 코드", example = "BILL01")
    private String invoiceCategoryCode;

    @Schema(description = "청구 유형 코드", example = "TYPE01")
    private String invoiceTypeCode;

    @Schema(description = "청구 분류 코드", example = "CLASS01")
    private String invoiceClassCode;

    @Schema(description = "관리번호 (Our Ref)", example = "MP-2026-0001")
    private String ourRef;

    @Schema(description = "상대방 관리번호 (Your Ref)", example = "YR-2026-9999")
    private String yourRef;

    @Schema(description = "고객사 관리번호 (Client Ref)", example = "CL-2026-5555")
    private String clientRef;

    @Schema(description = "부서명", example = "지식재산부")
    private String deptName;

    @Schema(description = "Debit No.", example = "D-2026-1111")
    private String debitNo;

    @Schema(description = "계산서 번호", example = "TAX-2026-0001")
    private String taxBillNo;

    @Schema(description = "계산서 유형 코드", example = "TAXTYPE01")
    private String taxBillTypeCode;

    @Schema(description = "계산서 구분 코드", example = "TAXDIV01")
    private String taxBillCategoryCode;

    @Schema(description = "OA 문서 코드", example = "OA01")
    private String oaDocument;

    @Schema(description = "청구 내용", example = "출원 비용 청구")
    private String invoiceContent;

    @Schema(description = "대리인 청구 구분 코드", example = "AGENT01")
    private String agentInvoiceCategoryCode;

    @Schema(description = "화폐 단위 코드", example = "KRW")
    private String currencyUnit;

    @Schema(description = "적용 환율", example = "1350.50")
    private BigDecimal exchangeRate;

    @Schema(description = "포기 내용", example = "절차 포기로 인한 청구 제외")
    private String giveUpContent;

    @Schema(description = "외주 내용", example = "번역 외주 비용 포함")
    private String outsourceContent;

    @Schema(description = "비고", example = "특이사항 없음")
    private String note;

    @Schema(description = "내국/인커밍/아웃고잉 구분", example = "INV")
    private String inOutType;

    @Schema(description = "사건 구분 코드", example = "60")
    private String caseCategoryCode;

    @Schema(description = "해외대리인 일련번호", example = "PAR20260000001", format = "SEQ")
    private String foreignAgentSeq;

    @Schema(description = "의뢰인 일련번호", example = "CUST20260000001", format = "SEQ")
    private String clientSeq;

    @Schema(description = "해외대리인", example = "Global Law")
    private String foreignAgent;

    @Schema(description = "의뢰인", example = "마인드프로")
    private String client;
}