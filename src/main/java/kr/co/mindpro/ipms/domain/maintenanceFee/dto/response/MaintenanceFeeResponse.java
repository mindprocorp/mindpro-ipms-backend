package kr.co.mindpro.ipms.domain.maintenanceFee.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * @author : seokho
 * @fileName : MaintenanceFeeResponse.java
 * @since : 2026. 4. 6.
 */
public class MaintenanceFeeResponse {
    @Builder
    public record MaintenanceFeeDetail(
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

    @Builder
    public record MaintenanceFeeList(
            @Schema(description = "유지비 시퀀스")
            String maintenanceFeeSeq,

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
