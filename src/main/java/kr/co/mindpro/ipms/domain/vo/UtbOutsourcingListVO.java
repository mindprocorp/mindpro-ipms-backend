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
public class UtbOutsourcingListVO extends BaseVO {
    private String outsourcingListSeq;

    private String participantSeq;

    private String userMstSeq;

    private String userInfo;

    private String officeSeq;

    private String outsourcingCorp;

    private Integer outsourcingCost;

    private Integer outsourcingVat;

    private String outsourcingContent;

    private LocalDateTime outsourcingDepositDate;
}