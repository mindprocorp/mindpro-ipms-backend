package kr.co.mindpro.ipms.domain.vo;

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
public class UtbAppGracePeriodVO extends BaseVO {
    private String appSeq;

    private String officeSeq;

    private String gracePeriodSeq;

    private LocalDateTime gracePeriodContent;

    private LocalDateTime submitDeadLineDate;

    private LocalDateTime submitClosingDate;
}