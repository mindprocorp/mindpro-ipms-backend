package kr.co.mindpro.ipms.domain.approval.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class ApprDocLineVO extends BaseVO {
    private String lineSeq;
    private String docSeq;
    private String stepOrder;
    private String stepName;
    /** APPROVAL | AGREEMENT | REVIEW */
    private String stepType;
    private String approverSeq;
    private String approverName;
    /** SELF | USER */
    private String approverType;
    /** PENDING | APPROVED | REJECTED */
    private String lineStatus;
    private OffsetDateTime actionAt;
    private String actionComment;
}
