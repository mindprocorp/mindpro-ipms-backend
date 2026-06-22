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
public class UtbOfficeMstVO extends BaseVO {
    private String officeSeq;

    private String officeShortName;

    private String officeAddr;

    private String officeTel;

    private String officeAuthYn;

    private String officeStateCode;
}