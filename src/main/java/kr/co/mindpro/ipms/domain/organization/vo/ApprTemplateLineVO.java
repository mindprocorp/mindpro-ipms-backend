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
public class ApprTemplateLineVO extends BaseVO {
    private String templateLineSeq;
    private String apprTemplateSeq;
    private String stepOrder;
    private String stepType;
    private String stepName;
    private String approverType;    // SELF, UPPER_DEPT_HEAD, DIRECT_PERSON, DEPT_HEAD
    private String approverRefSeq;  // 직접 지정 시 사원/부서 seq
    private String approverName;    // 표시명 (본인, 1차 상위 부서장, 홍길동 등)
}
