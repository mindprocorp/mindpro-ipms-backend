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
public class UtbApprLineVO extends BaseVO {
    private String apprLineSeq;

    private String officeSeq;

    private String officeEmployeeSeq;

    private String apprSeq;

    private String taskCategoryCode;

    private Integer apprIndex;

    private String state;
}