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
public class StbTlbCodeVO extends BaseVO {
    private String tblCodeSeq;

    private String tblName;

    private String tblShortName;

    private String tblMappingNm;
}