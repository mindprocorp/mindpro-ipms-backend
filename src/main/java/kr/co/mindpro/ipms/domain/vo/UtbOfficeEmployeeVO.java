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
public class UtbOfficeEmployeeVO extends BaseVO {
    private String officeSeq;

    private String officeEmployeeSeq;

    private String userMstSeq;

    private String adminAuth;

    private String officeEmployeePosition;

    private String officeEmployeeDept;

    private String workCode;

    private String positionCode;

    private String jobGradeCode;

    private String orgCode;
}