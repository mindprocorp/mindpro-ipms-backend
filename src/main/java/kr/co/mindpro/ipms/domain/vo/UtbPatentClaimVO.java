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
public class UtbPatentClaimVO extends BaseVO {
    private String patentClaimSeq;

    private String specificationSeq;

    private String appSeq;

    private String officeSeq;

    private String patentClaimCategoryCode;

    private String patentClaimNo;

    private String patentClaimDependentClaimNo;

    private String patentClaimContent;
}