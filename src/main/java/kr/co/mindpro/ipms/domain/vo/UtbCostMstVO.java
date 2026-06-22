package kr.co.mindpro.ipms.domain.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class UtbCostMstVO extends BaseVO {
    private String costSeq;

    private String costCategoryCode;

    private Long krwAmount;

    private BigDecimal discountRatio;

    private Integer remittanceCount;

    private LocalDateTime costDate;

    private String costRemittanceCount;

    private LocalDateTime costRemittanceDate;

    private String costRemittanceYn;

    private Integer costFee;

    private Integer costVat;
}