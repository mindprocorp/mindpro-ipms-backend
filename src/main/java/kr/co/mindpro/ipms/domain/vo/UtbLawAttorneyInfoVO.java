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
public class UtbLawAttorneyInfoVO extends BaseVO {
    private String userMstSeq;

    private String lawyerAffiliation;

    private String lawyerRegNo;

    private String digitalLitigationAuthNo;
}