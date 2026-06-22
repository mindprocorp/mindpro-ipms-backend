package kr.co.mindpro.ipms.domain.vo;

import java.math.BigDecimal;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class UtbParticipantVO extends BaseVO {
    private String participantSeq;

    private String userMstSeq;

    private String userInfo;

    private String officeSeq;

    private String roleCode;

    private Integer shareRatio;
}