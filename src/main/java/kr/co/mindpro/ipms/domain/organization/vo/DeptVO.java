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
public class DeptVO extends BaseVO {
    private String deptSeq;
    private String officeSeq;
    private String parentDeptSeq;
    private String deptCode;
    private String deptName;
    private String deptPath;
    private String depth;
    private String sortOrd;
    private String useYn;
}
