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
public class UtbProprresearchMstVO extends BaseVO {
    private String officeSeq;

    private String priorresearchSeq;

    private String priorresearchState;

    private String priorresearchNo;

    private String priorresearchCategoryCode;

    private String priorresearchGoal;

    private String priorresearchRetaincontent;

    private String priorresearchRetainFile;
}