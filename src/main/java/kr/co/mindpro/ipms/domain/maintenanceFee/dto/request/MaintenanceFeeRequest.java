package kr.co.mindpro.ipms.domain.maintenanceFee.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author : seokho
 * @fileName : MaintenanceFeeRequest.java
 * @since : 2026. 4. 2.
 */
public class MaintenanceFeeRequest {
    public record CreateMaintenanceFeeRequest(
            @Schema(description = "유지비 시퀀스")
            String maintenanceFeeSeq,

            @Schema(description = "출원 시퀀스")
            String appSeq,

            @Schema(description = "차기납부차수")
            Integer nextPaymentInstallment,

            // 납부마감일
            @Schema(description = "납부마감일")
            String maintFeeDeadline,

            // 과태마감일
            @Schema(description = "과태마감일")
            String maintFeePenaltyDeadline,

            // 납부지시일
            @Schema(description = "납부지시일")
            String maintFeeOrderDate,

            // 납부일
            @Schema(description = "납부일")
            String maintFeePaymentDate,

            @Schema(description = "비고")
            String note
    ) {}
}
