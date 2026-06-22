package kr.co.mindpro.ipms.domain.approval.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class ApprDocTargetVO extends BaseVO {
    private String targetSeq;
    private String docSeq;
    /** RECEIVE | SHARE */
    private String targetRole;
    /** EMPLOYEE | DEPT */
    private String targetType;
    private String refSeq;
    private String refName;   // JOIN
}
