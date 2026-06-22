package kr.co.mindpro.ipms.domain.history.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ModifiedHistVO extends BaseVO {
    private String modifiedHistSeq;
    private String officeSeq;
    private String tblSeq;
    private String beforeValue;
    private String afterValue;
    private String modifiedContent;
    private String note;
    private String modifiedDate;
}
