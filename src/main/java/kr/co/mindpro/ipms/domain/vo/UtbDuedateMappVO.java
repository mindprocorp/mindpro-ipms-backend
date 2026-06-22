package kr.co.mindpro.ipms.domain.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class UtbDuedateMappVO extends BaseVO {
    private String officeSeq;

    private String mappingDuedateSeq;

    private String tblSeq;

    private String participantSeq;

    private String dueDateMstSeq;

    private String userInfoSeq;
}