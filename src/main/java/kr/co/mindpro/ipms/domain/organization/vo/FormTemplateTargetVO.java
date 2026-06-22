package kr.co.mindpro.ipms.domain.organization.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class FormTemplateTargetVO extends BaseVO {
    private String targetSeq;
    private String formTemplateSeq;
    private String targetRole;   // SHARE_GROUP | WRITE_AUTH | READ_AUTH | RECEIVE
    private String targetType;   // EMPLOYEE | DEPT_HEAD | DEPT
    private String refSeq;

    // JOIN 결과 (DB 저장 안 됨)
    private String refName;
    private String refDept;
    private String refPosition;
    private String refEmail;
    private String refMobile;
}
