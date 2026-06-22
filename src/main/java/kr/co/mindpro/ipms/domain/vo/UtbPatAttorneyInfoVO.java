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
public class UtbPatAttorneyInfoVO extends BaseVO {
    private String userMstSeq;

    private String patAttorneyRegNo;

    private String patAttorneyAffiliation;

    private String patAttorneyDigitalSign;

    private String patAttorneyTechSpecialtyCategory;

    private String patAttorneySpecialtyCategory;

    private Integer patAttorneyYearCnt;

    private String patAttorneyDescription;
}