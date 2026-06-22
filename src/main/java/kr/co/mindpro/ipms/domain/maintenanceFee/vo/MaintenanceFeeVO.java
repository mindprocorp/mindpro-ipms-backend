package kr.co.mindpro.ipms.domain.maintenanceFee.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author : seokho
 * @fileName : MaintenanceFeeVO.java
 * @since : 2026. 4. 2.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "유지비 정보 객체")
public class MaintenanceFeeVO extends BaseVO {

    @Schema(description = "사무소 시퀀스")
    private String officeSeq;

    @Schema(description = "유지비 시퀀스")
    private String maintenanceFeeSeq;

    @Schema(description = "출원 시퀀스")
    private String appSeq;

    @Schema(description = "차기납부차수")
    private Integer nextPaymentInstallment;

    // 아래는 duedate에 저장되는 항목들
    @Schema(description = "납부마감일")
    private String maintFeeDeadline;

    @Schema(description = "과태마감일")
    private String maintFeePenaltyDeadline;

    @Schema(description = "납부지시일")
    private String maintFeeOrderDate;

    @Schema(description = "납부일")
    private String maintFeePaymentDate;
}
