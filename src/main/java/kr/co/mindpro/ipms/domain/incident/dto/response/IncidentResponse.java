package kr.co.mindpro.ipms.domain.incident.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import lombok.Builder;

public class IncidentResponse {

    @Builder
    public record IncidentClaimDetail(
            @Schema(description = "청구서 마스터 시퀀스", example = "INVMST20260000001", format = "SEQ")
            String invoiceSeq,
            CommonRecordResponse.CodeInfo costCategory,  // 비용구분
            String itemContent,       // 청구내용
            String note,               // 비고
            String invNo,              // 청구번호
            String invDate,            // 청구일
            String govFee,             // 관납료
            String agencyFee,          // 수수료
            String vat,                // 부가세
            String etcFee,             // 기타비용
            String transFee,           // 송금수수료
            String totalAmount,        // 합계
            String depAmount,          // 입금액
            String unpaidAmount,       // 미수금
            String abandonAmount,      // 포기금액
            String taxBillDate,        // 세금계산서일자
            String inOutType           // 국내외구분
    ) {}
}
