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
public class UtbPaperMappVO extends BaseVO {
    private String officeSeq;

    private String tblSeq;

    private String mappingPaperSeq;

    private String userInfo;

    private String participantSeq;

    private String paperCategoryCode;

    private String userMstSeq;
}