package kr.co.mindpro.ipms.domain.organization.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class ApprTemplateVO extends BaseVO {
    private String apprTemplateSeq;
    private String officeSeq;
    private String templateName;
    private String sortOrd;

    private List<ApprTemplateLineVO> lines;
}
