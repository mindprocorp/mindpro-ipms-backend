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
public class UtbStaffVO extends BaseVO {
    private String officeSeq;

    private String officeEmployeeSeq;

    private String staffSeq;

    private String retainSeq;

    private String roleCode;
}