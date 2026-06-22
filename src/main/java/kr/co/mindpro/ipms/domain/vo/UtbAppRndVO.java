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
public class UtbAppRndVO extends BaseVO {
    private String appSeq;

    private String officeSeq;

    private String rndSeq;

    private String projectNo;

    private String researchNo;

    private String rndName;

    private LocalDateTime rndStartDate;

    private LocalDateTime rndClosingDate;

    private String mainLab;

    private String performingLab;

    private BigDecimal shareRatio;
}